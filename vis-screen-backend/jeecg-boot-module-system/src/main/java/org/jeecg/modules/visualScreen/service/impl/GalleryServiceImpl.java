package org.jeecg.modules.visualScreen.service.impl;

import org.jeecg.modules.util.PageData;
import org.jeecg.modules.visualScreen.mapper.GalleryMapper;
import org.jeecg.modules.visualScreen.service.GalleryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class GalleryServiceImpl implements GalleryService {

    @Autowired
    private GalleryMapper galleryMapper;

    @Override
    public List<Map<String, Object>> getPage(PageData pd) {
        return galleryMapper.getPage(pd);
    }

    @Override
    public Integer getCount(PageData pd) {
        return galleryMapper.getCount(pd);
    }

    @Override
    public List<Map<String, Object>> getAll(PageData pd) {
        return galleryMapper.getAll(pd);
    }

    @Override
    public void add(PageData pd) {
        galleryMapper.add(pd);
    }

    @Override
    public void edit(PageData pd) {
        galleryMapper.edit(pd);
    }

    @Override
    public void del(PageData pd) {
        galleryMapper.del(pd);
    }
}
