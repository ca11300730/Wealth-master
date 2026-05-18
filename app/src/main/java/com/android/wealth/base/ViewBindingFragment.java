package com.android.wealth.base;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.viewbinding.ViewBinding;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;

public abstract class ViewBindingFragment<T extends ViewBinding> extends BaseFragment {

    private T mViewBinding;

    @SuppressWarnings("unchecked")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        Class<?> clazz = getViewBindingClass();
        try {
            Method[] methods = clazz.getMethods();
            for (Method m : methods) {
                Class<?>[] parameterTypes = m.getParameterTypes();
                if (parameterTypes.length == 3
                        && parameterTypes[0] == LayoutInflater.class
                        && parameterTypes[1] == ViewGroup.class
                        && parameterTypes[2] == boolean.class) {
                    mViewBinding = (T) m.invoke(null, inflater, container, false);
                    return mViewBinding.getRoot();
                }
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
        throw new RuntimeException("Not find inflate(LayoutInflater,ViewGroup,boolean) method");
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    protected final T getViewBinding() {
        return mViewBinding;
    }

    private Class<?> getViewBindingClass() {
        Class<?> clazz = this.getClass();
        while (clazz.getSuperclass() != ViewBindingFragment.class) {
            clazz = clazz.getSuperclass();
        }
        ParameterizedType type = (ParameterizedType) clazz.getGenericSuperclass();
        return (Class<?>) type.getActualTypeArguments()[0];
    }

    public void showToast(CharSequence toast){
        Toast.makeText(getActivity(), toast, Toast.LENGTH_SHORT).show();
    }



}
