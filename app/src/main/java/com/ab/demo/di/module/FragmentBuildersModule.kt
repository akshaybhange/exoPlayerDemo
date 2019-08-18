package com.ab.demo.di.module

import com.ab.demo.ui.player.PlayerFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector


@Suppress("unused")
@Module
abstract class FragmentBuildersModule {
    @ContributesAndroidInjector
    abstract fun contributePlayerFragment(): PlayerFragment

}