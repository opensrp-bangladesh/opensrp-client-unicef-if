package org.ei.opensrp.cursoradapter;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;

import org.ei.opensrp.commonregistry.CommonPersonObject;
import org.ei.opensrp.commonregistry.CommonPersonObjectClient;
import org.ei.opensrp.commonregistry.CommonRepository;
import org.ei.opensrp.view.contract.SmartRegisterClient;

/**
 * Created by raihan on 3/9/16.
 */
public class SmartRegisterPaginatedCursorAdapter extends CursorAdapter {
    private final SmartRegisterCLientsProviderForCursorAdapter listItemProvider;
    Context context;
    CommonRepository commonRepository;

    public SmartRegisterPaginatedCursorAdapter(Context context, Cursor c,SmartRegisterCLientsProviderForCursorAdapter listItemProvider,CommonRepository commonRepository) {
        super(context, c);
        this.listItemProvider = listItemProvider;
        this.context= context;
        this.commonRepository = commonRepository;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
      return  listItemProvider.inflatelayoutForCursorAdapter();
//        return null;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        CommonPersonObject personinlist = commonRepository.readAllcommonforCursorAdapter(cursor);
        CommonPersonObjectClient pClient = new CommonPersonObjectClient(personinlist.getCaseId(), personinlist.getDetails(), personinlist.getDetails().get("FWHOHFNAME"));
        pClient.setColumnmaps(personinlist.getColumnmaps());
       listItemProvider.getView(pClient,view);

    }

    @Override
    public Cursor swapCursor(Cursor newCursor) {
        return super.swapCursor(newCursor);
    }
}
