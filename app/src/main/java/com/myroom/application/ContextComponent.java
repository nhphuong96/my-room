package com.myroom.application;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {ContextModule.class})
public interface ContextComponent {
    void inject(BaseApplication baseApplication);
    Context getContext();
}
