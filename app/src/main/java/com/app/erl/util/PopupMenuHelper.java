package com.app.erl.util;

public class PopupMenuHelper {

//    public static void showPopupMenu(Context mContext, View view, List<ModuleInfo> list, int dialogIdentifier) {
//        PopupMenu popupMenu = new PopupMenu(mContext, view);
//        for (int i = 0; i < list.size(); i++) {
//            popupMenu.getMenu().add(Menu.NONE, i, i, list.get(i).getName());
//        }
//
//        popupMenu.setOnMenuItemClickListener(menuItem -> {
//            int position = menuItem.getItemId();
//            ModuleSelection moduleSelection = new ModuleSelection(list.get(position).getId(), list.get(position).getName(), list.get(position).getImage(), dialogIdentifier, true, null,list.get(position).getProject_name());
//            EventBus.getDefault().post(moduleSelection);
//            return false;
//        });
//
//        popupMenu.show();
//    }
}
