package org.ei.opensrp.dghs.hh_member;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.TextView;

import org.ei.opensrp.commonregistry.CommonPersonObjectClient;
import org.ei.opensrp.cursoradapter.SmartRegisterCLientsProviderForCursorAdapter;
import org.ei.opensrp.dghs.R;
import org.ei.opensrp.domain.Alert;
import org.ei.opensrp.service.AlertService;
import org.ei.opensrp.util.DateUtil;
import org.ei.opensrp.view.contract.SmartRegisterClient;
import org.ei.opensrp.view.contract.SmartRegisterClients;
import org.ei.opensrp.view.customControls.CustomFontTextView;
import org.ei.opensrp.view.dialog.FilterOption;
import org.ei.opensrp.view.dialog.ServiceModeOption;
import org.ei.opensrp.view.dialog.SortOption;
import org.ei.opensrp.view.viewHolder.OnClickFormLauncher;

import java.util.List;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static org.ei.opensrp.util.StringUtil.humanize;

/**
 * Created by user on 2/12/15.
 */
public class HH_member_detail_SmartClientsProvider implements SmartRegisterCLientsProviderForCursorAdapter {

    private final LayoutInflater inflater;
    private final Context context;
    private final View.OnClickListener onClickListener;

    private final int txtColorBlack;
    private final AbsListView.LayoutParams clientViewLayoutParams;
    private final AlertService alertService;


    public HH_member_detail_SmartClientsProvider(Context context,
                                                 View.OnClickListener onClickListener,
                                                 AlertService alertService) {
        this.onClickListener = onClickListener;
        this.alertService = alertService;
        this.context = context;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        clientViewLayoutParams = new AbsListView.LayoutParams(MATCH_PARENT,
                (int) context.getResources().getDimension(org.ei.opensrp.R.dimen.list_item_height));
        txtColorBlack = context.getResources().getColor(org.ei.opensrp.R.color.text_black);
    }

    @Override
    public void getView(final SmartRegisterClient smartRegisterClient, View convertView) {
        View itemView = convertView;

//        LinearLayout profileinfolayout = (LinearLayout)itemView.findViewById(R.id.profile_info_layout);

        ImageView profilepic = (ImageView)itemView.findViewById(R.id.profilepic);
        TextView name = (TextView)itemView.findViewById(R.id.name);
        TextView uniqueid = (TextView)itemView.findViewById(R.id.unique_id);
        TextView age = (TextView)itemView.findViewById(R.id.age);
        TextView maritalstatus = (TextView)itemView.findViewById(R.id.marital_status);
        TextView general = (TextView)itemView.findViewById(R.id.general);
        TextView newborn_or_fp = (TextView)itemView.findViewById(R.id.newborn_pr_fp);




//        TextView psrfdue = (TextView)itemView.findViewById(R.id.psrf_due_date);
////        Button due_visit_date = (Button)itemView.findViewById(R.id.hh_due_date);
//
//        ImageButton follow_up = (ImageButton)itemView.findViewById(R.id.btn_edit);


        final CommonPersonObjectClient pc = (CommonPersonObjectClient) smartRegisterClient;
        general.setOnClickListener(onClickListener);
        general.setTag(smartRegisterClient);

        newborn_or_fp.setOnClickListener(onClickListener);
        newborn_or_fp.setTag(smartRegisterClient);

        profilepic.setOnClickListener(onClickListener);
        profilepic.setTag(smartRegisterClient);


        if((pc.getDetails().get("Is_child")!=null?pc.getDetails().get("Is_child"):"").equalsIgnoreCase("1")){
            name.setText(pc.getColumnmaps().get("Member_Fname")!=null?pc.getColumnmaps().get("Member_Fname"):"");
            uniqueid.setVisibility(View.GONE);
            age.setText(pc.getColumnmaps().get("Child_calc_age")!=null?pc.getColumnmaps().get("Child_calc_age"):"");
            try {
                int days = DateUtil.dayDifference(DateUtil.getLocalDate((pc.getDetails().get("Child_dob") != null ?  pc.getDetails().get("Child_dob")  : "")), DateUtil.today());
                int calc_age = days / 365;
                age.setText(calculateage(days));
            }catch (Exception e){

            }
            maritalstatus.setVisibility(View.GONE);
            if ((pc.getDetails().get("Child_gender") != null ? pc.getDetails().get("Child_gender") : "").equalsIgnoreCase("1")) {
                profilepic.setImageResource(R.drawable.child_boy_infant);
//                newborn_or_fp.setText("Family Planning");
                newborn_or_fp.setVisibility(View.INVISIBLE);
            } else {
                profilepic.setImageResource(R.drawable.child_girl_infant);
//                newborn_or_fp.setVisibility(View.INVISIBLE);
                newborn_or_fp.setVisibility(View.INVISIBLE);
            }
            if (pc.getDetails().get("profilepic") != null) {
                HH_member_SmartRegisterActivity.setImagetoHolderFromUri((Activity)context, pc.getDetails().get("profilepic"), profilepic, R.drawable.child_boy_infant);
            }
//

        }else {
            name.setText(pc.getColumnmaps().get("Member_Fname") != null ? pc.getColumnmaps().get("Member_Fname") : "");

            String unique_id_string = "NID : "+ (pc.getDetails().get("Member_NID") != null ? pc.getDetails().get("Member_NID") : "");
            unique_id_string = unique_id_string + " / " + "BRID : "+ (pc.getDetails().get("Member_BRID") != null ? pc.getDetails().get("Member_BRID") : "");
            unique_id_string = unique_id_string + " / " + "BDH : "+ (pc.getDetails().get("Member_HID") != null ? pc.getDetails().get("Member_HID") : "");

            uniqueid.setText(unique_id_string);
            age.setText(pc.getColumnmaps().get("calc_age_confirm") != null ? pc.getColumnmaps().get("calc_age_confirm") : "");
            try {
                int days = DateUtil.dayDifference(DateUtil.getLocalDate((pc.getDetails().get("calc_dob_confirm") != null ?  pc.getDetails().get("calc_dob_confirm")  : "")), DateUtil.today());
                int calc_age = days / 365;
                age.setText(calc_age);
            }catch (Exception e){

            }
//            maritalstatus.setText(pc.getColumnmaps().get("Marital_Status") != null ? pc.getColumnmaps().get("Marital_Status") : "null");
            if ((pc.getDetails().get("Is_woman") != null ? pc.getDetails().get("Is_woman") : "").equalsIgnoreCase("1")) {
                profilepic.setImageResource(R.drawable.woman_placeholder);
//                newborn_or_fp.setText("Family Planning");
                newborn_or_fp.setVisibility(View.INVISIBLE);
            } else {
                profilepic.setImageResource(R.mipmap.household_profile_thumb);
                newborn_or_fp.setVisibility(View.INVISIBLE);
            }


            if (pc.getDetails().get("profilepic") != null) {
                HH_member_SmartRegisterActivity.setImagetoHolderFromUri((Activity)context, pc.getDetails().get("profilepic"), profilepic, R.mipmap.womanimageload);
            }


            if((pc.getColumnmaps().get("Marital_status")!=null?pc.getColumnmaps().get("Marital_status"):"").equalsIgnoreCase("1")){
                maritalstatus.setText("Unmarried");
            }
            else if((pc.getColumnmaps().get("Marital_status")!=null?pc.getColumnmaps().get("Marital_status"):"").equalsIgnoreCase("2")){
                maritalstatus.setText("Married");
            }else if ((pc.getColumnmaps().get("Marital_status")!=null?pc.getColumnmaps().get("Marital_status"):"").equalsIgnoreCase("3")){
                maritalstatus.setText("Divorced/Widow/Widower");
            }
        }

//        dateofbirth.setText(mcaremotherObject.getColumnmaps().get("FWBNFDTOO")!=null?mcaremotherObject.getColumnmaps().get("FWBNFDTOO"):"");




//        constructRiskFlagView(pc, itemView);
//        constructENCCReminderDueBlock(pc, itemView);
////        constructNBNFDueBlock(pc, itemView);s
//        constructENCCVisitStatusBlock(pc,itemView);




        itemView.setLayoutParams(clientViewLayoutParams);
    }

    private String calculateage(int i) {
        if(i <= 15){
            return (i + " days");
        }
        if(i <= 141){
            return (i/7 + " weeks");
        }
        if(i <= 719){
            return (i/30 + " months");
        }
        if(i >719){
            String years = 719/365 + " years ";
            String months = "";
            if((719%365)!=0) {
                months = (719 % 365)/30 + " months";
            }

            return years + months;
        }
        return "";
    }


    private void checkEncc1StatusAndform(TextView anc1tick, TextView anc1text, CommonPersonObjectClient pc) {
        if(pc.getDetails().get("FWENC1DATE")!=null){
            anc1text.setText("ENCC1-"+pc.getDetails().get("FWENC1DATE"));
            if(pc.getDetails().get("encc1_current_formStatus")!=null){
                if(pc.getDetails().get("encc1_current_formStatus").equalsIgnoreCase("upcoming")){

                }else if(pc.getDetails().get("encc1_current_formStatus").equalsIgnoreCase("urgent")){
                    anc1tick.setTextColor(context.getResources().getColor(R.color.alert_urgent_red));
                    anc1text.setText("urgent");
                }
            }
        }else{
            List<Alert> alertlist = org.ei.opensrp.Context.getInstance().alertService().findByEntityIdAndAlertNames(pc.entityId(), "enccrv_1");
            String alertstate = "";
            String alertDate = "";
            if(alertlist.size()!=0){
                for(int i = 0;i<alertlist.size();i++){
                    alertstate = alertlist.get(i).status().value();
                    alertDate = alertlist.get(i).startDate();
                }              ;
            }
            if(alertstate != null && !(alertstate.trim().equalsIgnoreCase(""))){
                if(alertstate.equalsIgnoreCase("expired")){
                    anc1tick.setText("✘");
                    anc1tick.setTextColor(context.getResources().getColor(R.color.alert_urgent_red));
                    anc1text.setText( "ENCC1-" + alertDate);
//                    (anc+ "-"+alertlist.get(i).startDate(),alertlist.get(i).status().value())
                }else {
                    anc1text.setVisibility(View.GONE);
                    anc1tick.setVisibility(View.GONE);
                }
            } else {
                anc1text.setVisibility(View.GONE);
                anc1tick.setVisibility(View.GONE);
            }
        }
    }
    private void checkEncc2StatusAndform(TextView anc2tick, TextView anc2text, CommonPersonObjectClient pc) {
        if(pc.getDetails().get("FWENC2DATE")!=null){
            anc2text.setText("ENCC2-"+pc.getDetails().get("FWENC2DATE"));
            if(pc.getDetails().get("encc2_current_formStatus")!=null){
                if(pc.getDetails().get("encc2_current_formStatus").equalsIgnoreCase("upcoming")){

                }else if(pc.getDetails().get("encc2_current_formStatus").equalsIgnoreCase("urgent")){
                    anc2tick.setTextColor(context.getResources().getColor(R.color.alert_urgent_red));
                }
            }
        }else{
            List<Alert> alertlist = org.ei.opensrp.Context.getInstance().alertService().findByEntityIdAndAlertNames(pc.entityId(), "enccrv_2");
            String alertstate = "";
            String alertDate = "";
            if(alertlist.size()!=0){
                for(int i = 0;i<alertlist.size();i++){
                    alertstate = alertlist.get(i).status().value();
                    alertDate = alertlist.get(i).startDate();
                }              ;
            }
            if(alertstate != null && !(alertstate.trim().equalsIgnoreCase(""))){
                if(alertstate.equalsIgnoreCase("expired")){
                    anc2tick.setText("✘");
                    anc2tick.setTextColor(context.getResources().getColor(R.color.alert_urgent_red));
                    anc2text.setText( "ENCC2-" + alertDate);
//                    (anc+ "-"+alertlist.get(i).startDate(),alertlist.get(i).status().value())
                }else {
                    anc2text.setVisibility(View.GONE);
                    anc2tick.setVisibility(View.GONE);
                }
            } else {
                anc2text.setVisibility(View.GONE);
                anc2tick.setVisibility(View.GONE);
            }
        }
    }
    private void checkEncc3StatusAndform(TextView anc3tick, TextView anc3text, CommonPersonObjectClient pc) {
        if(pc.getDetails().get("FWENC3DATE")!=null){
            anc3text.setText("ENCC3-"+pc.getDetails().get("FWENC3DATE"));
            if(pc.getDetails().get("encc3_current_formStatus")!=null){
                if(pc.getDetails().get("encc3_current_formStatus").equalsIgnoreCase("upcoming")){

                }else if(pc.getDetails().get("encc3_current_formStatus").equalsIgnoreCase("urgent")){
                    anc3tick.setTextColor(context.getResources().getColor(R.color.alert_urgent_red));
                }
            }
        }else{
            List<Alert> alertlist = org.ei.opensrp.Context.getInstance().alertService().findByEntityIdAndAlertNames(pc.entityId(), "enccrv_3");
            String alertstate = "";
            String alertDate = "";
            if(alertlist.size()!=0){
                for(int i = 0;i<alertlist.size();i++){
                    alertstate = alertlist.get(i).status().value();
                    alertDate = alertlist.get(i).startDate();
                }              ;
            }
            if(alertstate != null && !(alertstate.trim().equalsIgnoreCase(""))){
                if(alertstate.equalsIgnoreCase("expired")){
                    anc3tick.setText("✘");
                    anc3tick.setTextColor(context.getResources().getColor(R.color.alert_urgent_red));
                    anc3text.setText( "ENCC3-" + alertDate);
//                    (anc+ "-"+alertlist.get(i).startDate(),alertlist.get(i).status().value())
                }else {
                    anc3text.setVisibility(View.GONE);
                    anc3tick.setVisibility(View.GONE);
                }
            } else {
                anc3text.setVisibility(View.GONE);
                anc3tick.setVisibility(View.GONE);
            }
        }
    }

    private void constructENCCReminderDueBlock(CommonPersonObjectClient pc, View itemView) {
        alertTextandStatus alerttextstatus = null;
            List<Alert> alertlist3 = org.ei.opensrp.Context.getInstance().alertService().findByEntityIdAndAlertNames(pc.entityId(), "enccrv_3");
            if(alertlist3.size() != 0){
                alerttextstatus = setAlertStatus("ENCC3",alertlist3);
            }else{
                List<Alert> alertlist2 = org.ei.opensrp.Context.getInstance().alertService().findByEntityIdAndAlertNames(pc.entityId(), "enccrv_2");
                if(alertlist2.size()!=0){
                    alerttextstatus = setAlertStatus("ENCC2",alertlist2);
                }else{
                    List<Alert> alertlist = org.ei.opensrp.Context.getInstance().alertService().findByEntityIdAndAlertNames(pc.entityId(), "enccrv_1");
                    if(alertlist.size()!=0){
                        alerttextstatus = setAlertStatus("ENCC1",alertlist);

                    }else{
                        alerttextstatus = new alertTextandStatus("Not synced","not synced");
                    }
                }
            }

//        CustomFontTextView pncreminderDueDate = (CustomFontTextView)itemView.findViewById(R.id.encc_reminder_due_date);
//        setalerttextandColorInView(pncreminderDueDate, alerttextstatus,pc);


    }

    private void setalerttextandColorInView(CustomFontTextView customFontTextView, alertTextandStatus alerttextstatus, CommonPersonObjectClient pc) {
        customFontTextView.setText(alerttextstatus.getAlertText() != null ? alerttextstatus.getAlertText() : "");
        if(alerttextstatus.getAlertstatus().equalsIgnoreCase("normal")){
            customFontTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
            customFontTextView.setBackgroundColor(context.getResources().getColor(org.ei.opensrp.R.color.alert_upcoming_light_blue));
        }
        if(alerttextstatus.getAlertstatus().equalsIgnoreCase("upcoming")){
            customFontTextView.setBackgroundColor(context.getResources().getColor(R.color.alert_upcoming_yellow));
            customFontTextView.setOnClickListener(onClickListener);
//            customFontTextView.setTag(R.id.clientobject, pc);
//            customFontTextView.setTag(R.id.textforEnccRegister, alerttextstatus.getAlertText() != null ? alerttextstatus.getAlertText() : "");
//            customFontTextView.setTag(R.id.AlertStatustextforEnccRegister,"upcoming");
        }
        if(alerttextstatus.getAlertstatus().equalsIgnoreCase("urgent")){
            customFontTextView.setOnClickListener(onClickListener);
//            customFontTextView.setTag(R.id.clientobject, pc);
//            customFontTextView.setTag(R.id.textforEnccRegister,alerttextstatus.getAlertText() != null ? alerttextstatus.getAlertText() : "");
//            customFontTextView.setBackgroundColor(context.getResources().getColor(org.ei.opensrp.R.color.alert_urgent_red));
//            customFontTextView.setTag(R.id.AlertStatustextforEnccRegister, "urgent");

        }
        if(alerttextstatus.getAlertstatus().equalsIgnoreCase("expired")){
            customFontTextView.setBackgroundColor(context.getResources().getColor(org.ei.opensrp.R.color.client_list_header_dark_grey));
            customFontTextView.setText("expired");
            customFontTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }
        if(alerttextstatus.getAlertstatus().equalsIgnoreCase("complete")){
//               psrfdue.setText("visited");
            customFontTextView.setBackgroundColor(context.getResources().getColor(R.color.alert_complete_green_mcare));
            customFontTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }
        if(alerttextstatus.getAlertstatus().equalsIgnoreCase("not synced")){
            customFontTextView.setBackgroundColor(context.getResources().getColor(org.ei.opensrp.R.color.status_bar_text_almost_white));
//
        }
    }

    private alertTextandStatus setAlertStatus(String anc, List<Alert> alertlist) {
        alertTextandStatus alts = null;
        for(int i = 0;i<alertlist.size();i++){
            alts = new alertTextandStatus(anc+ "-"+alertlist.get(i).startDate(),alertlist.get(i).status().value());
            }
        return alts;
    }

    private void constructRiskFlagView(CommonPersonObjectClient pc,  View itemView) {
//        AllCommonsRepository allancRepository = org.ei.opensrp.Context.getInstance().allCommonsRepositoryobjects("mcaremother");
//        CommonPersonObject ancobject = allancRepository.findByCaseID(pc.entityId());
//        AllCommonsRepository allelcorep = org.ei.opensrp.Context.getInstance().allCommonsRepositoryobjects("elco");
//        CommonPersonObject elcoparent = allelcorep.findByCaseID(ancobject.getRelationalId());

        ImageView hrp = (ImageView)itemView.findViewById(R.id.hrp);
        ImageView hp = (ImageView)itemView.findViewById(R.id.hr);
        ImageView vg = (ImageView)itemView.findViewById(R.id.vg);
        if(pc.getDetails().get("FWVG") != null && pc.getDetails().get("FWVG").equalsIgnoreCase("1")){

        }else{
            vg.setVisibility(View.GONE);
        }
        if(pc.getDetails().get("FWHRP") != null && pc.getDetails().get("FWHRP").equalsIgnoreCase("1")){

        }else{
            hrp.setVisibility(View.GONE);
        }
        if(pc.getDetails().get("FWHR_PSR") != null && pc.getDetails().get("FWHR_PSR").equalsIgnoreCase("1")){

        }else{
            hp.setVisibility(View.GONE);
        }

//        if(pc.getDetails().get("FWWOMAGE")!=null &&)

    }


    @Override
    public SmartRegisterClients updateClients(FilterOption villageFilter, ServiceModeOption serviceModeOption,
                                              FilterOption searchFilter, SortOption sortOption) {
        return null;
    }

    @Override
    public void onServiceModeSelected(ServiceModeOption serviceModeOption) {
        // do nothing.
    }

    @Override
    public OnClickFormLauncher newFormLauncher(String formName, String entityId, String metaData) {
        return null;
    }

    public LayoutInflater inflater() {
        return inflater;
    }

    @Override
    public View inflatelayoutForCursorAdapter() {
        return (ViewGroup) inflater().inflate(R.layout.smart_register_dghs_hh_details_client, null);
    }

    class alertTextandStatus{
        String alertText ,alertstatus;

        public alertTextandStatus(String alertText, String alertstatus) {
            this.alertText = alertText;
            this.alertstatus = alertstatus;
        }

        public String getAlertText() {
            return alertText;
        }

        public void setAlertText(String alertText) {
            this.alertText = alertText;
        }

        public String getAlertstatus() {
            return alertstatus;
        }

        public void setAlertstatus(String alertstatus) {
            this.alertstatus = alertstatus;
        }
    }
}
