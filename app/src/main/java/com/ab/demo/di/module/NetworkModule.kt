package com.ab.demo.di.module

import dagger.Module


@Suppress("unused")
@Module
class NetworkModule {
    companion object {
        private const val MUSIC_URL = "https://bitmovin-a.akamaihd.net/content/playhouse-vr/m3u8s/105560.m3u8"
    }


}