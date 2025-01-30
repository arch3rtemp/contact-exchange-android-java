package dev.arch3rtemp.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

import dev.arch3rtemp.ui.R;
import dev.arch3rtemp.ui.util.ColorUtils;

public class AppSearchView extends SearchView {

    public AppSearchView(@NonNull Context context) {
        super(context);
        init();
    }

    public AppSearchView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public AppSearchView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        TextView searchTextView = findViewById(androidx.appcompat.R.id.search_src_text);
        var font = ResourcesCompat.getFont(getContext(), R.font.poppins_medium);
        var textColor = ContextCompat.getColor(getContext(), R.color.warm_grey_two);
        var hintColor = ContextCompat.getColor(getContext(), R.color.pinkish_grey);
        searchTextView.setTypeface(font);
        searchTextView.setTextSize(12f);
        searchTextView.setTextColor(textColor);

        ImageView closeIcon = findViewById(androidx.appcompat.R.id.search_close_btn);
        closeIcon.setColorFilter(ColorUtils.createSrcInColorFilter(hintColor));

        ImageView searchIcon = findViewById(androidx.appcompat.R.id.search_button);
        int iconColor = ContextCompat.getColor(getContext(), R.color.icon_primary);
        var iconDrawable = ContextCompat.getDrawable(getContext(), R.drawable.ic_search);
        assert iconDrawable != null;
        iconDrawable.setColorFilter(ColorUtils.createSrcInColorFilter(iconColor));
        searchIcon.setImageDrawable(iconDrawable);
    }
}
