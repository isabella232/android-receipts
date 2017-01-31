package tech.receipts.components.autocomplete;

import android.content.Context;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import tech.receipts.R;
import tech.receipts.data.model.autocomplete.Autocomplete;
import tech.receipts.injection.ApplicationContext;
import timber.log.Timber;

public class TaxAutocompleteAdapter {
    private Set<String> set = new HashSet<>();
    private Context context;

    @Inject
    public TaxAutocompleteAdapter(@ApplicationContext Context context) {
        this.context = context;
    }

    public ArrayAdapter<String> getAdapter() {
        List<String> adapterList = new ArrayList<String>(set.size());
        adapterList.addAll(set);
        Timber.d("getAdapter: count: %d", adapterList.size());
        return new ArrayAdapter(context, R.layout.autocomplete_list, adapterList);
    }

    public void update(List<String> list) {
        set.clear();
        set.addAll(list);
    }

    public void add(Autocomplete autocomplete) {
        set.add(autocomplete.getTax());
    }
}
