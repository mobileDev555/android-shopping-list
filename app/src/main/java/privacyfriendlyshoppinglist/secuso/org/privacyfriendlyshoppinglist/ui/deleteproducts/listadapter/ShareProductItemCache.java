package privacyfriendlyshoppinglist.secuso.org.privacyfriendlyshoppinglist.ui.deleteproducts.listadapter;

import androidx.cardview.widget.CardView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;

import privacyfriendlyshoppinglist.secuso.org.privacyfriendlyshoppinglist.R;

class ShareProductItemCache
{
    private TextView productNameTextView;
    private TextView productQuantityTextView;
    private CheckBox checkBox;
    private CardView productCard;
    private ImageButton shareImageButton;

    public ShareProductItemCache(View parent)
    {
        productNameTextView = (TextView) parent.findViewById(R.id.textview_product_name);
        productQuantityTextView = (TextView) parent.findViewById(R.id.textview_prod_quantity);
        checkBox = (CheckBox) parent.findViewById(R.id.checkbox_is_selected);
        productCard = (CardView) parent.findViewById(R.id.cardview_item);
        shareImageButton = (ImageButton) parent.findViewById(R.id.share_button_list);
    }

    public TextView getProductNameTextView()
    {
        return productNameTextView;
    }

    public TextView getProductQuantityTextView()
    {
        return productQuantityTextView;
    }

    public CheckBox getCheckBox()
    {
        return checkBox;
    }

    public ImageButton getShareImageButton()
    {
        return shareImageButton;
    }

    public CardView getProductCard()
    {
        return productCard;
    }
}
