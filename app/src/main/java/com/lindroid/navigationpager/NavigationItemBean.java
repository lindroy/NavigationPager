package com.lindroid.navigationpager;

/**
 * Created by Lindroid on 2017/3/20.
 */

public class NavigationItemBean {
    private int iconID;
    private String iconName;
    private boolean isSelected;

    public NavigationItemBean(int iconID, String iconName) {
        this.iconID = iconID;
        this.iconName = iconName;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public int getIconID() {
        return iconID;
    }

    public void setIconID(int iconID) {
        this.iconID = iconID;
    }

    public String getIconName() {
        return iconName;
    }

    public void setIconName(String iconName) {
        this.iconName = iconName;
    }
}
