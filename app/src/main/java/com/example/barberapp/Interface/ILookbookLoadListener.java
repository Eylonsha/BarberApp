package com.example.barberapp.Interface;

import java.util.List;

import com.example.barberapp.Model.Banner;

public interface ILookbookLoadListener {
    void onLookbookLoadSuccess(List<Banner> banners);
    void onLookbookLoadFailed(String message);
}
