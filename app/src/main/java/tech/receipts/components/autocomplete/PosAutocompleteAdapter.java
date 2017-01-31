package tech.receipts.components.autocomplete;

import android.content.Context;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.inject.Inject;

import tech.receipts.R;
import tech.receipts.data.model.autocomplete.Autocomplete;
import tech.receipts.injection.ApplicationContext;
import timber.log.Timber;

public class PosAutocompleteAdapter {

    private ConcurrentHashMap<String, Autocomplete> map = new ConcurrentHashMap<>();
    private Context context;

    @Inject
    public PosAutocompleteAdapter(@ApplicationContext Context context) {
        this.context = context;
    }

    public void update(List<Autocomplete> list) {
        map.clear();
        for (Autocomplete autocomplete : list) {
            map.putIfAbsent(autocomplete.getPos(), autocomplete);
        }
    }

    public ArrayAdapter<String> getAdapter(String tax) {
        Timber.d("Filter after tax: '%s' map of data: '%s'", tax, map);
        List<String> adapterList = new ArrayList<String>(map.size());

        for (Map.Entry<String, Autocomplete> entry : map.entrySet()) {
            Autocomplete item = entry.getValue();

            if (item.getTax().equalsIgnoreCase(tax)) {
                Timber.d("Add item: '%s'", item);
                adapterList.add(item.getPos());
            }
        }

        Timber.d("getAdapter: count: %d", adapterList.size());
        return new ArrayAdapter(context, R.layout.autocomplete_list, adapterList);
    }

    public void add(Autocomplete autocomplete) {
        map.putIfAbsent(autocomplete.getPos(), autocomplete);
    }
}

