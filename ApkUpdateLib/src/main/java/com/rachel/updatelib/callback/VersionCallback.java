package com.rachel.updatelib.callback;

import com.rachel.updatelib.enties.FileInfo;
import com.rachel.updatelib.enties.LocalInfo;

/**
 * Created by zhengshaorui on 2017/4/20.
 */

public interface VersionCallback {
    void success(FileInfo fileInfo, LocalInfo localInfo);
    void lastest();
}
