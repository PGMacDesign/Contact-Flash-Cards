package pgmacdesign.contactflashcards;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.pgmacdesign.pgmactips.adaptersandlisteners.CustomClickCallbackLink;
import com.pgmacdesign.pgmactips.adaptersandlisteners.CustomClickListener;
import com.pgmacdesign.pgmactips.adaptersandlisteners.CustomLongClickCallbackLink;
import com.pgmacdesign.pgmactips.adaptersandlisteners.CustomLongClickListener;
import com.pgmacdesign.pgmactips.utilities.AnimationUtilities;
import com.pgmacdesign.pgmactips.utilities.ContactUtilities;
import com.pgmacdesign.pgmactips.utilities.ImageUtilities;
import com.pgmacdesign.pgmactips.utilities.MiscUtilities;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pmacdowell on 2018-04-20.
 */

public class AdapterContacts extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<MyPojo> mListObjects;
    private boolean oneSelectedAnimate;
    private Context context;
    private CustomClickCallbackLink customClickCallbackLink;
    private CustomLongClickCallbackLink customLongClickCallbackLink;
    private int profileImageDefault;
    private LayoutInflater mInflater;

    public AdapterContacts(@NonNull Context context, CustomClickCallbackLink customClickCallbackLink,
                           CustomLongClickCallbackLink customLongClickCallbackLink){
        this.customClickCallbackLink = customClickCallbackLink;
        this.customLongClickCallbackLink = customLongClickCallbackLink;
        this.context = context;
        this.profileImageDefault = R.drawable.default_profile;
        this.mInflater = LayoutInflater.from(this.context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        switch (viewType){

            case 0: //AdapterContactsTypes.Picture.getAdapterType()
                view = mInflater.inflate(R.layout.picture_contact_holder_layout, parent, false);
                return new PictureContactHolder(view);

            case 1: //AdapterContactsTypes.Phone.getAdapterType()
                view = mInflater.inflate(R.layout.phone_contact_holder_layout, parent, false);
                return new PhoneContactHolder(view);

            case 2: //AdapterContactsTypes.Email.getAdapterType()
                view = mInflater.inflate(R.layout.email_contact_holder_layout, parent, false);
                return new EmailContactHolder(view);

            default:
            case 3: //AdapterContactsTypes.Full.getAdapterType()
                view = mInflater.inflate(R.layout.full_contact_holder_layout, parent, false);
                return new FullContactHolder(view);

        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(position < 0 || position >= getItemCount() || MiscUtilities.isListNullOrEmpty(mListObjects)){
            return;
        }
        MyPojo pojo = mListObjects.get(position);
        if(pojo == null){
            return;
        }
        ContactUtilities.Contact contact = pojo.getContact();
        if(contact == null){
            return;
        }

        String photoUri = contact.getPhotoUri();
        String name = contact.getRawDisplayName();
        String phone = contact.getSimplifiedPhoneNumber();
        String email = contact.getSimplifiedEmail();

        CustomClickListener clickListener = new CustomClickListener(
                customClickCallbackLink, 1, pojo, position);
        CustomLongClickListener longClickListener = new CustomLongClickListener(
                customLongClickCallbackLink, 1, pojo, position);

        switch (pojo.getType()){

            case Picture:
                PictureContactHolder holder0 = (PictureContactHolder) holder;
                ImageUtilities.setCircularImageWithPicasso(photoUri,
                        holder0.picture_contact_holder_iv, profileImageDefault, context);
                holder0.picture_contact_holder_root.setOnClickListener(clickListener);
                if(this.oneSelectedAnimate){
                    this.oneSelectedAnimate = false;
                    AnimationUtilities.animateMyView(holder0.picture_contact_holder_root,
                            700, Techniques.FlipInX);
                }
                break;

            case Phone:
                PhoneContactHolder holder1 = (PhoneContactHolder) holder;
                holder1.phone_contact_holder_tv.setText("Phone:\n" + phone);
                holder1.phone_contact_holder_root.setOnClickListener(clickListener);
                if(this.oneSelectedAnimate){
                    this.oneSelectedAnimate = false;
                    AnimationUtilities.animateMyView(holder1.phone_contact_holder_root,
                            700, Techniques.Pulse);
                }
                break;

            case Email:
                EmailContactHolder holder2 = (EmailContactHolder) holder;
                holder2.phone_contact_holder_tv.setText("Email:\n" + email);
                holder2.email_contact_holder_root.setOnClickListener(clickListener);
                if(this.oneSelectedAnimate){
                    this.oneSelectedAnimate = false;
                    AnimationUtilities.animateMyView(holder2.email_contact_holder_root,
                            700, Techniques.RotateIn);
                }
                break;

            default:
            case Full:
                FullContactHolder holder3 = (FullContactHolder) holder;
                ImageUtilities.setCircularImageWithPicasso(photoUri,
                        holder3.full_contact_holder_iv, profileImageDefault, context);
                holder3.full_contact_holder_email.setText("Email:\n" + email);
                holder3.full_contact_holder_phone.setText("Phone:\n" + phone);
                holder3.full_contact_holder_email.setText("Name:\n" + name);
                holder3.full_contact_holder_root.setOnClickListener(clickListener);
                if(this.oneSelectedAnimate){
                    this.oneSelectedAnimate = false;
                    AnimationUtilities.animateMyView(holder3.full_contact_holder_root,
                            700, Techniques.FlipInX);
                }

                break;

        }
    }


    public void setData(List<MyPojo> mListObjects){
        this.mListObjects = (MiscUtilities.isListNullOrEmpty(mListObjects))
                ? new ArrayList<MyPojo>() : mListObjects;
        this.notifyDataSetChanged();
    }

    public void updateOneObject(MyPojo pojo, int pos){
        if(pojo != null){
            if(pos >= 0 && pos < getItemCount() && !MiscUtilities.isListNullOrEmpty(mListObjects)){
                this.oneSelectedAnimate = true;
                this.mListObjects.set(pos, pojo);
                this.notifyItemChanged(pos);
            }
        }
    }

    public void removeOneObject(int pos){
        if(pos >= 0 && pos < getItemCount() && !MiscUtilities.isListNullOrEmpty(mListObjects)){
            this.oneSelectedAnimate = true;
            this.mListObjects.remove(pos);
            this.notifyDataSetChanged();
        }
    }

    @Override
    public int getItemCount() {
        return (MiscUtilities.isListNullOrEmpty(mListObjects))
                ? 0 : mListObjects.size();
    }

    @Override
    public int getItemViewType(int position) {

        try {
            switch (mListObjects.get(position).getType()){

                case Picture:
                    return AdapterContactsTypes.Picture.getAdapterType();

                case Phone:
                    return AdapterContactsTypes.Phone.getAdapterType();

                case Email:
                    return AdapterContactsTypes.Email.getAdapterType();

                default:
                case Full:
                    return AdapterContactsTypes.Full.getAdapterType();

            }
        } catch (Exception e){
            return super.getItemViewType(position);
        }
    }

    public static enum AdapterContactsTypes {
        Picture(0), Phone(1), Email(2), Full(3);

        private int adapterType;
        AdapterContactsTypes(int adapterType){
            this.adapterType = adapterType;
        }

        public int getAdapterType(){
            return this.adapterType;
        }
    }

    private class FullContactHolder extends RecyclerView.ViewHolder {

        private RelativeLayout full_contact_holder_root;
        private LinearLayout full_contact_holder_left_layout, full_contact_holder_right_layout;
        private ImageView full_contact_holder_iv;
        private TextView full_contact_holder_name, full_contact_holder_phone, full_contact_holder_email;

        public FullContactHolder(View itemView) {
            super(itemView);

            this.full_contact_holder_iv = (ImageView) itemView.findViewById(
                    R.id.full_contact_holder_iv);
            this.full_contact_holder_root = (RelativeLayout) itemView.findViewById(
                    R.id.full_contact_holder_root);
            this.full_contact_holder_left_layout = (LinearLayout) itemView.findViewById(
                    R.id.full_contact_holder_left_layout);
            this.full_contact_holder_right_layout = (LinearLayout) itemView.findViewById(
                    R.id.full_contact_holder_right_layout);
            this.full_contact_holder_name = (TextView) itemView.findViewById(
                    R.id.full_contact_holder_name);
            this.full_contact_holder_email = (TextView) itemView.findViewById(
                    R.id.full_contact_holder_email);
            this.full_contact_holder_phone = (TextView) itemView.findViewById(
                    R.id.full_contact_holder_phone);
        }
    }


    private class PictureContactHolder extends RecyclerView.ViewHolder {

        private RelativeLayout picture_contact_holder_root;
        private ImageView picture_contact_holder_iv;

        public PictureContactHolder(View itemView) {
            super(itemView);

            this.picture_contact_holder_iv = (ImageView) itemView.findViewById(
                    R.id.picture_contact_holder_iv);
            this.picture_contact_holder_root = (RelativeLayout) itemView.findViewById(
                    R.id.picture_contact_holder_root);
        }
    }


    private class PhoneContactHolder extends RecyclerView.ViewHolder {

        private RelativeLayout phone_contact_holder_root;
        private TextView phone_contact_holder_tv;

        public PhoneContactHolder(View itemView) {
            super(itemView);

            this.phone_contact_holder_tv = (TextView) itemView.findViewById(
                    R.id.phone_contact_holder_tv);
            this.phone_contact_holder_root = (RelativeLayout) itemView.findViewById(
                    R.id.phone_contact_holder_root);
        }
    }


    private class EmailContactHolder extends RecyclerView.ViewHolder {

        private RelativeLayout email_contact_holder_root;
        private TextView phone_contact_holder_tv;

        public EmailContactHolder(View itemView) {
            super(itemView);

            this.phone_contact_holder_tv = (TextView) itemView.findViewById(
                    R.id.phone_contact_holder_tv);
            this.email_contact_holder_root = (RelativeLayout) itemView.findViewById(
                    R.id.email_contact_holder_root);
        }
    }
}
