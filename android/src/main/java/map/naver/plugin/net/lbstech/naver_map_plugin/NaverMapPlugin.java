package map.naver.plugin.net.lbstech.naver_map_plugin;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import androidx.annotation.NonNull;

import java.util.concurrent.atomic.AtomicInteger;

import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.embedding.engine.plugins.activity.ActivityAware;
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding;

/** NaverMapPlugin */
public class NaverMapPlugin implements FlutterPlugin, Application.ActivityLifecycleCallbacks,
        ActivityAware {

  static final int CREATED = 1;
  static final int STARTED = 2;
  static final int RESUMED = 3;
  static final int PAUSED = 4;
  static final int STOPPED = 5;
  static final int SAVE_INSTANCE_STATE = 6;
  static final int DESTROYED = 7;

  private final AtomicInteger state = new AtomicInteger(0);
  private int registrarActivityHashCode;
  private FlutterPluginBinding pluginBinding;
  private ActivityPluginBinding activityPluginBinding;

  /// constructor
  public NaverMapPlugin(){}

  private NaverMapPlugin(Activity activity) {
    this.registrarActivityHashCode = activity.hashCode();
  }

  @Override
  public void onAttachedToEngine(@NonNull FlutterPluginBinding binding) {
    pluginBinding = binding;
  }

  // 플러그인 등록
  @Override
  public void onAttachedToActivity(@NonNull ActivityPluginBinding binding) {
    activityPluginBinding = binding;
    registrarActivityHashCode = binding.getActivity().hashCode();
    binding.getActivity().getApplication().registerActivityLifecycleCallbacks(this);

    pluginBinding
            .getPlatformViewRegistry()
            .registerViewFactory(
                    "naver_map_plugin",
                    new NaverMapFactory(
                            state,
                            pluginBinding.getBinaryMessenger(),
                            binding.getActivity()
                    ));
  }

  @Override
  public void onDetachedFromActivityForConfigChanges() {
    activityPluginBinding.getActivity().getApplication().unregisterActivityLifecycleCallbacks(this);
  }

  @Override
  public void onReattachedToActivityForConfigChanges(ActivityPluginBinding binding) {
    activityPluginBinding = binding;
    binding.getActivity().getApplication().registerActivityLifecycleCallbacks(this);
  }

  @Override
  public void onDetachedFromActivity() {
    activityPluginBinding.getActivity().getApplication().unregisterActivityLifecycleCallbacks(this);
  }

  @Override
  public void onDetachedFromEngine(@NonNull FlutterPluginBinding binding) {
    pluginBinding = null;
  }


  // ========================= ActivityLifeCycleCallBack =============================

  @Override
  public void onActivityCreated(Activity activity, Bundle bundle) {
    if (activity.hashCode() != registrarActivityHashCode) {
      return;
    }
    state.set(CREATED);
  }

  @Override
  public void onActivityStarted(Activity activity) {
    if (activity.hashCode() != registrarActivityHashCode) {
      return;
    }
    state.set(STARTED);
  }

  @Override
  public void onActivityResumed(Activity activity) {
    if (activity.hashCode() != registrarActivityHashCode) {
      return;
    }
    state.set(RESUMED);
  }

  @Override
  public void onActivityPaused(Activity activity) {
    if (activity.hashCode() != registrarActivityHashCode) {
      return;
    }
    state.set(PAUSED);
  }

  @Override
  public void onActivityStopped(Activity activity) {
    if (activity.hashCode() != registrarActivityHashCode) {
      return;
    }
    state.set(STOPPED);
  }

  @Override
  public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {
    if (activity.hashCode() != registrarActivityHashCode) {
      return;
    }
    state.set(SAVE_INSTANCE_STATE);
  }

  @Override
  public void onActivityDestroyed(Activity activity) {
    if (activity.hashCode() != registrarActivityHashCode) {
      return;
    }
    activity.getApplication().unregisterActivityLifecycleCallbacks(this);
    state.set(DESTROYED);
  }
}
