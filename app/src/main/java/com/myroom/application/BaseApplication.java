package com.myroom.application;

import android.app.Service;
import android.content.Context;

public class BaseApplication extends android.app.Application {

    private static ContextComponent contextComponent;
    private static RepositoryComponent repositoryComponent;
    private static ServiceComponent serviceComponent;

    @Override
    public void onCreate() {
        contextComponent = DaggerContextComponent.builder().contextModule(new ContextModule(this)).build();
        repositoryComponent = DaggerRepositoryComponent.builder().repositoryModule(new RepositoryModule(this)).build();
        serviceComponent = DaggerServiceComponent.builder().build();
        super.onCreate();
    }

    public static RepositoryComponent getRepositoryComponent(Context context) {
        return ((BaseApplication)context.getApplicationContext()).repositoryComponent;
    }

    public static ServiceComponent getServiceComponent(Context context) {
        return ((BaseApplication)context.getApplicationContext()).serviceComponent;
    }

    public static ContextComponent getContextComponent() {
        return contextComponent;
    }


}
