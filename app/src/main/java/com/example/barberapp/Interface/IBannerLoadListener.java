package com.example.barberapp.Interface;

import java.util.List;

import com.example.barberapp.Model.Banner;

public interface IBannerLoadListener {
    void onBannerLoadSuccess(List<Banner> banners);
    void onBannerLoadFailed(String message);
}
