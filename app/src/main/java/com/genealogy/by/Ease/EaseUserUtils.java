//package com.genealogy.by.Ease;
//
//import android.content.Context;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import com.bumptech.glide.Glide;
//import com.bumptech.glide.load.engine.DiskCacheStrategy;
//import com.bumptech.glide.request.RequestOptions;
//import com.genealogy.by.R;
//
//
//public class EaseUserUtils {
//
//    static EaseUI.EaseUserProfileProvider userProvider;
//
//    static {
//        userProvider = EaseUI.getInstance().getUserProfileProvider();
//    }
//
//    /**
//     * get EaseUser according username
//     * @param username
//     * @return
//     */
//    public static EaseUser getUserInfo(String username){
//        if(userProvider != null)
//            return userProvider.getUser(username);
//
//        return null;
//    }
//
//    /**
//     * set user avatar
//     * @param username
//     */
//    public static void setUserAvatar(Context context, String username, ImageView imageView){
//    	EaseUser user = getUserInfo(username);
//        if(user != null && user.getAvatar() != null){
//            try {
//                int avatarResId = Integer.parseInt(user.getAvatar());
//                Glide.with(context).load(avatarResId).into(imageView);
//            } catch (Exception e) {
//                //use default avatar
//                Glide.with(context).load(user.getAvatar())
//                        .apply(RequestOptions.placeholderOf(R.drawable.ease_default_avatar)
//                                .diskCacheStrategy(DiskCacheStrategy.ALL))
//                        .into(imageView);
//            }
//        }else{
//            Glide.with(context).load(R.drawable.ease_default_avatar).into(imageView);
//        }
//    }
//
//    /**
//     * set user's nickname
//     */
//    public static void setUserNick(String username, TextView textView){
//        if(textView != null){
//        	EaseUser user = getUserInfo(username);
//        	if(user != null && user.getNickname() != null){
//        		textView.setText(user.getNickname());
//        	}else{
//        		textView.setText(username);
//        	}
//        }
//    }
//
//}
