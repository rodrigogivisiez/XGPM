package de.Maxr1998.xposed.gpm.hooks;

import android.content.Context;

import java.io.File;
import java.util.UUID;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

import static de.Maxr1998.xposed.gpm.Common.ART_CACHE_OVERLAY_PATH;
import static de.Maxr1998.xposed.gpm.Common.GPM;
import static de.robv.android.xposed.XposedBridge.log;
import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;

public class ArtReplacer {

    public static void init(XC_LoadPackage.LoadPackageParam lPParam) {
        try {
            findAndHookMethod(GPM + ".download.cache.CacheUtils", lPParam.classLoader, "resolveArtPath", Context.class, String.class, int.class, UUID.class, new XC_MethodHook() {
                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    if (param.getResult() == null) {
                        return;
                    }
                    String path = ((File) param.getResult()).getAbsolutePath();
                    String fileName = path.substring(path.lastIndexOf("/") + 1);
                    File replacement = new File(ART_CACHE_OVERLAY_PATH + fileName);
                    if (replacement.exists()) {
                        log("Replacing " + fileName);
                        param.setResult(replacement);
                    }
                }
            });
        } catch (Throwable t) {
            log(t);
        }
    }
}
