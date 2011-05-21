/*
 *  Copyright (C) 2008-2011 Michael Kanis and others
 *  
 *  This file is part of Geoclipse.
 *
 *  Geoclipse is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, version 2 of the License, or
 *  (at your option) any later version.
 *
 *  Geoclipse is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with Geoclipse.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.skweez.geoclipse.map.preferences;

import java.io.File;
import java.text.NumberFormat;

import net.skweez.geoclipse.Activator;
import net.skweez.geoclipse.map.internal.MapImageCache;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.DirectoryFieldEditor;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.PlatformUI;

/**
 * @author Wolfgang Schramm
 */
public class PrefPageCache extends PreferencePage implements
		IWorkbenchPreferencePage {

	private static final String EMPTY_STRING = "";
	private static final String SIZE_MBYTE = " MByte";

	final static NumberFormat nf = NumberFormat.getNumberInstance();

	final String fDefaultCachePath = Platform.getInstanceLocation().getURL()
			.getPath();

	private Composite fPrefContainer;
	private Group fOfflineContainer;
	private BooleanFieldEditor fUseOffLineCache;
	private BooleanFieldEditor fUseDefaultLocation;
	private Composite fPathContainer;

	private DirectoryFieldEditor fCachePathEditor;

	private Label fLblInfoPath;
	private Label fLblInfoPathValue;
	private Label fLblInfoFiles;
	private Label fLblInfoFilesValue;
	private Label fLblInfoSize;
	private Label fLblInfoSizeValue;
	private Label fLblInfoWaiting;
	private Label fLblInfoWaitingValue;
	private Button fBtnOfflineCache;

	private int fFileCounter;
	private long fFileSize;
	private File fTileCacheDir;

	@Override
	protected Control createContents(final Composite parent) {

		createUI(parent);

		enableControls();

		getOfflineInfo();

		return fPrefContainer;
	}

	private void createUI(final Composite parent) {

		final IPreferenceStore prefStore = getPreferenceStore();

		fPrefContainer = new Composite(parent, SWT.NONE);
		GridDataFactory.swtDefaults().grab(true, false).applyTo(fPrefContainer);
		GridLayoutFactory.fillDefaults().applyTo(fPrefContainer);
		GridDataFactory.swtDefaults().applyTo(fPrefContainer);
		// fPrefContainer.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_YELLOW));

		// checkbox: is offline enabled
		fUseOffLineCache = new BooleanFieldEditor(
				IMappingPreferences.OFFLINE_CACHE_USE_OFFLINE,
				Messages.pref_cache_use_offline, fPrefContainer);
		fUseOffLineCache.setPreferenceStore(prefStore);
		fUseOffLineCache.setPage(this);
		fUseOffLineCache.load();
		fUseOffLineCache
				.setPropertyChangeListener(new IPropertyChangeListener() {
					public void propertyChange(final PropertyChangeEvent event) {
						enableControls();
					}
				});

		/*
		 * offline cache settings
		 */
		final Composite offlineContainer = new Composite(fPrefContainer,
				SWT.NONE);
		GridDataFactory.fillDefaults().grab(true, false).indent(15, 5)
				.applyTo(offlineContainer);
		GridLayoutFactory.fillDefaults().applyTo(offlineContainer);

		createUICacheSettings(offlineContainer);
		createUICacheInfo(offlineContainer);

		/*
		 * hide error messages, this happend when the cache path is invalid but
		 * the offline cache is disabled
		 */
		if (fUseOffLineCache.getBooleanValue() == false) {
			setErrorMessage(null);
		}
	}

	private void createUICacheInfo(final Composite parent) {

		final Group group = new Group(parent, SWT.NONE);
		group.setText("Offline Info");
		GridLayoutFactory.swtDefaults().numColumns(2).applyTo(group);
		GridDataFactory.fillDefaults().grab(true, false).applyTo(group);

		fLblInfoPath = new Label(group, SWT.NONE);
		fLblInfoPath.setText("Path:");
		fLblInfoPathValue = new Label(group, SWT.NONE);
		GridDataFactory.fillDefaults().grab(true, false)
				.applyTo(fLblInfoPathValue);

		fLblInfoFiles = new Label(group, SWT.NONE);
		fLblInfoFiles.setText("Files:");
		fLblInfoFilesValue = new Label(group, SWT.NONE);
		GridDataFactory.fillDefaults().applyTo(fLblInfoFilesValue);

		fLblInfoSize = new Label(group, SWT.NONE);
		fLblInfoSize.setText("Size:");
		fLblInfoSizeValue = new Label(group, SWT.NONE);
		GridDataFactory.fillDefaults().applyTo(fLblInfoSizeValue);

		fLblInfoWaiting = new Label(group, SWT.NONE);
		fLblInfoWaiting.setText("Status:");

		fLblInfoWaitingValue = new Label(group, SWT.NONE);
		fLblInfoWaitingValue.setText("retrieving...");
		GridDataFactory.fillDefaults().grab(true, false)
				.applyTo(fLblInfoWaitingValue);

		// button: delete offline files
		fBtnOfflineCache = new Button(group, SWT.PUSH);
		fBtnOfflineCache.setText(Messages.pref_cache_clear_cache);
		GridDataFactory.swtDefaults().span(2, 1).applyTo(fBtnOfflineCache);
		fBtnOfflineCache.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent e) {
				deleteOfflineFiles();
			}
		});
	}

	private void createUICacheSettings(final Composite parent) {

		final IPreferenceStore prefStore = getPreferenceStore();

		fOfflineContainer = new Group(parent, SWT.NONE);
		fOfflineContainer.setText("Offline Directory");
		GridDataFactory.fillDefaults().grab(true, false)
				.applyTo(fOfflineContainer);
		// GridLayoutFactory.swtDefaults().numColumns(1).applyTo(group);

		// field: use default location
		fUseDefaultLocation = new BooleanFieldEditor(
				IMappingPreferences.OFFLINE_CACHE_USE_DEFAULT_LOCATION,
				Messages.pref_cache_use_default_location, fOfflineContainer);
		fUseDefaultLocation.setPreferenceStore(prefStore);
		fUseDefaultLocation.setPage(this);
		fUseDefaultLocation.load();
		fUseDefaultLocation
				.setPropertyChangeListener(new IPropertyChangeListener() {
					public void propertyChange(final PropertyChangeEvent event) {
						enableControls();
					}
				});
		new Label(fOfflineContainer, SWT.NONE);

		fPathContainer = new Composite(fOfflineContainer, SWT.NONE);
		GridDataFactory.fillDefaults().grab(true, false).span(3, 1)
				.applyTo(fPathContainer);
		{
			// field: path for the tile cache
			fCachePathEditor = new DirectoryFieldEditor(
					IMappingPreferences.OFFLINE_CACHE_PATH,
					Messages.pref_cache_location, fPathContainer);
			fCachePathEditor.setPreferenceStore(prefStore);
			fCachePathEditor.setPage(this);
			fCachePathEditor.setEmptyStringAllowed(false);
			fCachePathEditor.load();
			fCachePathEditor
					.setPropertyChangeListener(new IPropertyChangeListener() {
						public void propertyChange(
								final PropertyChangeEvent event) {
							getOfflineInfo();
						}
					});
		}

		// !!! set layout after the editor was created because the editor sets
		// the parents layout
		GridLayoutFactory.swtDefaults().numColumns(3)
				.applyTo(fOfflineContainer);
	}

	/**
	 * Deletes all files and subdirectories. If a deletion fails, the method
	 * stops attempting to delete and returns false.
	 * 
	 * @param directory
	 * @return Returns <code>true</code> if all deletions were successful
	 */
	private boolean deleteDir(final File directory) {

		if (directory.isDirectory()) {

			final String[] children = directory.list();

			for (String element : children) {
				final boolean success = deleteDir(new File(directory, element));
				if (!success) {
					return false;
				}
			}
		}

		// The directory is now empty so delete it
		final boolean isDeleted = directory.delete();

		return isDeleted;
	}

	private void deleteOfflineFiles() {

		final Display display = Display.getCurrent();

		final MessageBox msgBox = new MessageBox(display.getActiveShell(),
				SWT.ICON_QUESTION | SWT.YES | SWT.NO);
		msgBox.setText("Confirm Offline Map Delete");
		msgBox.setMessage(NLS
				.bind("Are you sure to delete the folder \"{0}\" and all it's content?",
						fTileCacheDir.getAbsolutePath()));

		if (msgBox.open() == SWT.YES) {
			BusyIndicator.showWhile(display, new Runnable() {

				public void run() {

					fLblInfoWaitingValue.setText("deleding files...");
					fLblInfoWaitingValue.pack(true);

					deleteDir(fTileCacheDir);
					getOfflineInfo();
				}
			});
		}
	}

	/**
	 * Returns preference store that belongs to this plugin.
	 * 
	 * @return IPreferenceStore the preference store for this plugin
	 */
	@Override
	protected IPreferenceStore doGetPreferenceStore() {
		return Activator.getDefault().getPreferenceStore();
	}

	private void enableControls() {

		final boolean useOffLineCache = fUseOffLineCache.getBooleanValue();
		final boolean useDefaultLocation = fUseDefaultLocation
				.getBooleanValue();

		fUseDefaultLocation.setEnabled(useOffLineCache, fOfflineContainer);

		// enable cache path editor, set default path
		if (useOffLineCache) {
			if (useDefaultLocation) {
				fCachePathEditor.setEnabled(false, fPathContainer);
				fCachePathEditor.setStringValue(fDefaultCachePath);
			} else {
				fCachePathEditor.setEnabled(true, fPathContainer);
			}
		} else {
			fCachePathEditor.setEnabled(false, fPathContainer);
		}
	}

	/**
	 * Recursive funktion to count files/size
	 * 
	 * @param listOfFiles
	 */
	private void getFilesInfo(final File[] listOfFiles) {

		for (final File file : listOfFiles) {
			if (file.isFile()) {

				// file

				fFileCounter++;
				fFileSize += file.length();

			} else if (file.isDirectory()) {

				// directory

				getFilesInfo(file.listFiles());
			}
		}
	}

	private void getOfflineInfo() {

		final String workingDirectory = fCachePathEditor.getStringValue();

		// check if working directory is available
		if (new File(workingDirectory).exists() == false) {

			fLblInfoPathValue.setText(workingDirectory);
			fLblInfoPathValue.pack(true);

			fLblInfoWaitingValue.setText("directory is not available");
			fLblInfoWaitingValue.pack(true);

			offlineCacheIsInvalid();
			return;
		}

		final IPath tileCachePath = new Path(workingDirectory)
				.append(MapImageCache.TILE_OFFLINE_CACHE_OS_PATH);

		fLblInfoPathValue.setText(tileCachePath.toOSString());
		fLblInfoPathValue.pack(true);

		fTileCacheDir = tileCachePath.toFile();
		if (fTileCacheDir.exists() == false) {

			fLblInfoWaitingValue.setText("directory is not available");
			fLblInfoWaitingValue.pack(true);

			offlineCacheIsInvalid();
			return;
		}

		Display.getCurrent().asyncExec(new Runnable() {

			public void run() {

				BusyIndicator.showWhile(Display.getCurrent(), new Runnable() {

					public void run() {

						fFileCounter = 0;
						fFileSize = 0;

						getFilesInfo(fTileCacheDir.listFiles());

						nf.setMinimumIntegerDigits(0);
						nf.setMaximumFractionDigits(0);
						fLblInfoFilesValue.setText(nf.format(fFileCounter));
						fLblInfoFilesValue.pack(true);

						nf.setMinimumIntegerDigits(2);
						nf.setMaximumFractionDigits(2);
						fLblInfoSizeValue.setText(nf
								.format((float) fFileSize / 1024 / 1024)
								+ SIZE_MBYTE);
						fLblInfoSizeValue.pack(true);

						fLblInfoWaitingValue.setText(EMPTY_STRING);
						fLblInfoWaitingValue.pack(true);

						fBtnOfflineCache.setEnabled(true);
					}
				});
			}
		});
	}

	public void init(final IWorkbench workbench) {
	}

	/**
	 * Update offline cache info when the cache is invalid
	 */
	private void offlineCacheIsInvalid() {

		fLblInfoFilesValue.setText(EMPTY_STRING);
		fLblInfoFilesValue.pack(true);

		fLblInfoSizeValue.setText(EMPTY_STRING);
		fLblInfoSizeValue.pack(true);

		fBtnOfflineCache.setEnabled(false);
	}

	@Override
	public boolean okToLeave() {
		if (validateData() == false) {
			return false;
		}

		return super.okToLeave();
	}

	@Override
	protected void performDefaults() {

		fUseOffLineCache.loadDefault();
		fUseDefaultLocation.loadDefault();

		enableControls();

		super.performDefaults();
	}

	@Override
	public boolean performOk() {

		if (validateData() == false) {
			return false;
		}

		final IPreferenceStore prefStore = Activator.getDefault()
				.getPreferenceStore();
		boolean isModified = false;

		// check if the cache settings have changed
		if (prefStore.getBoolean(IMappingPreferences.OFFLINE_CACHE_USE_OFFLINE) != fUseOffLineCache
				.getBooleanValue()) {
			isModified = true;
		}
		if (prefStore
				.getBoolean(IMappingPreferences.OFFLINE_CACHE_USE_DEFAULT_LOCATION) != fUseDefaultLocation
				.getBooleanValue()) {
			isModified = true;
		}
		if (prefStore.getString(IMappingPreferences.OFFLINE_CACHE_PATH).equals(
				fCachePathEditor.getStringValue()) == false) {
			isModified = true;
		}

		fUseOffLineCache.store();
		fUseDefaultLocation.store();
		fCachePathEditor.store();

		if (isModified) {

			if (MessageDialog.openQuestion(Display.getDefault()
					.getActiveShell(), Messages.pref_cache_message_box_title,
					Messages.pref_cache_message_box_text)) {

				Display.getCurrent().asyncExec(new Runnable() {
					public void run() {
						PlatformUI.getWorkbench().restart();
					}
				});
			}
		}

		return super.performOk();
	}

	private boolean validateData() {

		boolean isValid = true;
		final boolean useOffLineCache = fUseOffLineCache.getBooleanValue();

		if (useOffLineCache
				&& fUseDefaultLocation.getBooleanValue() == false
				&& (!fCachePathEditor.isValid() || fCachePathEditor
						.getStringValue().trim().length() == 0)) {

			isValid = false;
			setErrorMessage(Messages.pref_error_invalid_path);
			fCachePathEditor.setFocus();
		}

		if (isValid) {
			setErrorMessage(null);
		}

		return isValid;
	}

}
