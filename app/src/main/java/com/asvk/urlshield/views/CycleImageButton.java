/*
 *
 *   Created by Amitskr & VnjVibhash on 2/21/24, 10:32 AM
 *   Copyright Ⓒ 2024. All rights reserved Ⓒ 2024 http://vivekajee.in/
 *   Last modified: 2/29/24, 1:59 PM
 *
 *   Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file
 *   except in compliance with the License. You may obtain a copy of the License at
 *   http://www.apache.org/licenses/LICENS... Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 *    either express or implied. See the License for the specific language governing permissions and
 *    limitations under the License.
 * /
 */

package com.asvk.urlshield.views;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.ImageButton;

import com.asvk.urlshield.utilities.Enums;

import java.util.List;

public class CycleImageButton<T extends Enums.ImageEnum> extends ImageButton {

    private List<T> states;
    private int currentState;

    public CycleImageButton(Context context) {
        super(context);
    }

    public CycleImageButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CycleImageButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setStates(List<T> states) {
        this.states = states;
        updateImageResource(0);
    }

    public void setCurrentState(T currentState) {
        updateImageResource(states.indexOf(currentState));
    }

    public T getCurrentState() {
        return states == null || states.isEmpty() ? null : states.get(currentState);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    @Override
    public boolean performClick() {
        updateImageResource(currentState + 1);
        return super.performClick();
    }

    private void updateImageResource(int newState) {
        if (states == null || states.isEmpty()) {
            setImageDrawable(null);
        } else {
            currentState = newState >= 0 ? newState % states.size() : 0;
            setImageResource(states.get(currentState).getImageResource());
        }
    }
}
