package com.devgeek.cataloguemovie;

import android.content.Intent;
import android.widget.RemoteViewsService;

/**
 * Created by GunSky7 on 6/25/2018.
 */

public class StackWidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new StackRemoteViewsFactory(this.getApplicationContext(), intent);
    }
}
