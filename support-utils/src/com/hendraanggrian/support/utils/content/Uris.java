/*
 * Copyright (c) 2015-present, Facebook, Inc.
 * All rights reserved.
 *
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree. An additional grant
 * of patent rights can be found in the PATENTS file in the same directory.
 */

package com.hendraanggrian.support.utils.content;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.annotation.AnyRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import static com.hendraanggrian.support.utils.Preconditions.checkNotNull;

/**
 * Taken from https://github.com/facebook/fresco/blob/master/fbcore/src/main/java/com/facebook/common/util/UriUtil.java.
 *
 * @author Hendra Anggrian (hendraanggrian@gmail.com)
 */
public final class Uris {

    public static final String SCHEME_HTTP = "http";
    public static final String SCHEME_HTTPS = "https";
    public static final String SCHEME_FILE = ContentResolver.SCHEME_FILE;
    public static final String SCHEME_CONTENT = ContentResolver.SCHEME_CONTENT;
    public static final String SCHEME_ASSET = "asset";
    public static final String SCHEME_RESOURCE = "res";
    public static final String SCHEME_QUALIFIED_RESOURCE = ContentResolver.SCHEME_ANDROID_RESOURCE;
    public static final String SCHEME_DATA = "data";

    private Uris() {
    }

    public static boolean isNetwork(@NonNull Uri uri) {
        checkNotNull(uri);
        String scheme = uri.getScheme();
        return SCHEME_HTTPS.equals(scheme) || SCHEME_HTTP.equals(scheme);
    }

    public static boolean isFile(@NonNull Uri uri) {
        checkNotNull(uri);
        String scheme = uri.getScheme();
        return SCHEME_FILE.equals(scheme);
    }

    public static boolean isContent(@NonNull Uri uri) {
        checkNotNull(uri);
        String scheme = uri.getScheme();
        return SCHEME_CONTENT.equals(scheme);
    }

    public static boolean isContact(@NonNull Uri uri) {
        return isContent(uri)
                && ContactsContract.AUTHORITY.equals(uri.getAuthority())
                && !uri.getPath().startsWith(Uri.withAppendedPath(ContactsContract.AUTHORITY_URI, "display_photo").getPath());
    }

    public static boolean isCamera(@NonNull Uri uri) {
        checkNotNull(uri);
        String uriString = uri.toString();
        return uriString.startsWith(MediaStore.Images.Media.EXTERNAL_CONTENT_URI.toString())
                || uriString.startsWith(MediaStore.Images.Media.INTERNAL_CONTENT_URI.toString());
    }

    public static boolean isAsset(@NonNull Uri uri) {
        checkNotNull(uri);
        String scheme = uri.getScheme();
        return SCHEME_ASSET.equals(scheme);
    }

    public static boolean isResource(@NonNull Uri uri) {
        checkNotNull(uri);
        String scheme = uri.getScheme();
        return SCHEME_RESOURCE.equals(scheme);
    }

    public static boolean isQualifiedResource(@NonNull Uri uri) {
        checkNotNull(uri);
        String scheme = uri.getScheme();
        return SCHEME_QUALIFIED_RESOURCE.equals(scheme);
    }

    public static boolean isData(@NonNull Uri uri) {
        checkNotNull(uri);
        return SCHEME_DATA.equals(uri.getScheme());
    }

    @Nullable
    public static String getActualPath(@NonNull Context context, Uri uri) {
        checkNotNull(context);
        return getActualPath(context.getContentResolver(), uri);
    }

    @Nullable
    public static String getActualPath(@NonNull ContentResolver resolver, Uri uri) {
        checkNotNull(resolver);
        checkNotNull(uri);

        String result = null;
        if (isContent(uri)) {
            Cursor cursor = null;
            try {
                cursor = resolver.query(uri, null, null, null, null);
                if (cursor != null && cursor.moveToFirst()) {
                    int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                    if (idx != -1)
                        result = cursor.getString(idx);
                }
            } finally {
                if (cursor != null)
                    cursor.close();
            }
        } else if (isFile(uri)) {
            result = uri.getPath();
        }
        return result;
    }

    @NonNull
    public static Uri fromResourceId(@AnyRes int resourceId) {
        return new Uri.Builder()
                .scheme(SCHEME_RESOURCE)
                .path(String.valueOf(resourceId))
                .build();
    }

    @NonNull
    public static Uri fromQualifiedResource(@NonNull Context context, @AnyRes int resourceId) {
        checkNotNull(context);
        return fromQualifiedResource(context.getPackageName(), resourceId);
    }

    @NonNull
    public static Uri fromQualifiedResource(@NonNull String packageName, @AnyRes int resourceId) {
        checkNotNull(packageName);
        return new Uri.Builder()
                .scheme(SCHEME_QUALIFIED_RESOURCE)
                .authority(packageName)
                .path(String.valueOf(resourceId))
                .build();
    }
}