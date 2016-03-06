/*
 * Copyright 2016 Hippo Seven
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.hippo.ehviewer.client.parser;

import com.hippo.ehviewer.client.EhUtils;
import com.hippo.ehviewer.client.data.GalleryInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class GalleryApiParser {

    public static void parse(String body, List<GalleryInfo> galleryInfoList) throws JSONException {
        JSONObject jo = new JSONObject(body);
        JSONArray ja = jo.getJSONArray("gmetadata");

        for (int i = 0, length = ja.length(); i < length; i++) {
            JSONObject g = ja.getJSONObject(i);
            long gid = g.getLong("gid");
            GalleryInfo gi = getGalleryInfoByGid(galleryInfoList, gid);
            if (gi == null) {
                continue;
            }
            gi.title = g.getString("title");
            gi.titleJpn = g.getString("title_jpn");
            gi.category = EhUtils.getCategory(g.getString("category"));
            gi.thumb = g.getString("thumb");
            gi.uploader = g.getString("uploader");
            gi.posted = g.getString("posted"); // TODO long to date
            gi.rating = Float.parseFloat(g.getString("rating"));
            // tags
            JSONArray tagJa = g.getJSONArray("tags");
            int tagLength = tagJa.length();
            String[] tags = new String[tagLength];
            for (int j = 0; j < tagLength; j++) {
                tags[j] = tagJa.getString(j);
            }
            gi.simpleTags = tags;
        }
    }

    private static GalleryInfo getGalleryInfoByGid(List<GalleryInfo> galleryInfoList, long gid) {
        for (int i = 0, size = galleryInfoList.size(); i < size; i++) {
            GalleryInfo gi = galleryInfoList.get(i);
            if (gi.gid == gid) {
                return gi;
            }
        }
        return null;
    }
}