package com.example.data

import android.content.Context
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface CyberDao {
    // Themes
    @Query("SELECT * FROM themes ORDER BY isFavorite DESC, lastUsedTimestamp DESC, downloadsCount DESC")
    fun observeAllThemes(): Flow<List<ThemeEntity>>

    @Query("SELECT * FROM themes WHERE id = :themeId LIMIT 1")
    suspend fun getThemeById(themeId: String): ThemeEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertThemes(themes: List<ThemeEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTheme(theme: ThemeEntity)

    @Update
    suspend fun updateTheme(theme: ThemeEntity)

    @Query("DELETE FROM themes WHERE id = :themeId AND isBuiltIn = 0")
    suspend fun deleteCustomTheme(themeId: String)

    // Custom Widgets
    @Query("SELECT * FROM custom_widgets ORDER BY createdAt DESC")
    fun observeCustomWidgets(): Flow<List<CustomWidgetEntity>>

    @Query("SELECT * FROM custom_widgets ORDER BY createdAt DESC")
    suspend fun getCustomWidgetsOnce(): List<CustomWidgetEntity>

    @Query("SELECT * FROM custom_widgets WHERE boundAppWidgetId = :appWidgetId LIMIT 1")
    suspend fun getWidgetForAppWidgetId(appWidgetId: Int): CustomWidgetEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCustomWidget(widget: CustomWidgetEntity): Long

    @Query("DELETE FROM custom_widgets WHERE id = :widgetId")
    suspend fun deleteCustomWidget(widgetId: Int)

    // Custom Icons
    @Query("SELECT * FROM custom_icons")
    fun observeCustomIcons(): Flow<List<CustomIconOverrideEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCustomIcon(icon: CustomIconOverrideEntity)

    @Query("DELETE FROM custom_icons WHERE targetPackageOrId = :targetId")
    suspend fun deleteCustomIcon(targetId: String)

    // Wallpapers
    @Query("SELECT * FROM wallpaper_history ORDER BY isFavorite DESC, lastAppliedAt DESC")
    fun observeWallpapers(): Flow<List<WallpaperHistoryEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWallpapers(wallpapers: List<WallpaperHistoryEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWallpaper(wallpaper: WallpaperHistoryEntity)

    // Launcher Folders
    @Query("SELECT * FROM launcher_folders ORDER BY id ASC")
    fun observeLauncherFolders(): Flow<List<LauncherFolderEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLauncherFolder(folder: LauncherFolderEntity)

    @Query("DELETE FROM launcher_folders WHERE id = :folderId")
    suspend fun deleteLauncherFolder(folderId: Int)

    // Backup Snapshots
    @Query("SELECT * FROM backup_snapshots ORDER BY createdAt DESC")
    fun observeBackups(): Flow<List<BackupSnapshotEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBackup(backup: BackupSnapshotEntity)

    @Query("DELETE FROM backup_snapshots WHERE id = :backupId")
    suspend fun deleteBackup(backupId: Int)
}

@Database(
    entities = [
        ThemeEntity::class,
        CustomWidgetEntity::class,
        CustomIconOverrideEntity::class,
        WallpaperHistoryEntity::class,
        LauncherFolderEntity::class,
        BackupSnapshotEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class CyberDatabase : RoomDatabase() {
    abstract fun cyberDao(): CyberDao

    companion object {
        @Volatile
        private var INSTANCE: CyberDatabase? = null

        fun getInstance(context: Context): CyberDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    CyberDatabase::class.java,
                    "cyber_theme_x.db"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
