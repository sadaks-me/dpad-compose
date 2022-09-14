package dev.berggren.ui.menu

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal abstract class MenuManagerModule {

    @Binds
    abstract fun menuManager(menuManager: MenuManagerImpl): MenuManager
}
