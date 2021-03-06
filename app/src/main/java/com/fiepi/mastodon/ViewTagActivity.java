/* Copyright 2017 Andrew Dawson
 *
 * This file is a part of Tusky.
 *
 * This program is free software; you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation; either version 3 of the
 * License, or (at your option) any later version.
 *
 * Tusky is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even
 * the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General
 * Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with Tusky; if not,
 * see <http://www.gnu.org/licenses>. */

package com.fiepi.mastodon;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ViewTagActivity extends BaseActivity implements SFragment.OnUserRemovedListener {
    private Fragment timelineFragment;

    @BindView(R.id.toolbar) Toolbar toolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_tag);

        ButterKnife.bind(this);

        String hashtag = getIntent().getStringExtra("hashtag");

        setSupportActionBar(toolbar);
        ActionBar bar = getSupportActionBar();

        if (bar != null) {
            bar.setTitle(String.format(getString(R.string.title_tag), hashtag));
            bar.setDisplayHomeAsUpEnabled(true);
            bar.setDisplayShowHomeEnabled(true);
        }

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        Fragment fragment = TimelineFragment.newInstance(TimelineFragment.Kind.TAG, hashtag);
        fragmentTransaction.add(R.id.fragment_container, fragment);
        fragmentTransaction.commit();

        timelineFragment = fragment;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                onBackPressed();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onUserRemoved(String accountId) {
        StatusRemoveListener listener = (StatusRemoveListener) timelineFragment;
        listener.removePostsByUser(accountId);
    }
}
