package privacyfriendlyshoppinglist.secuso.org.privacyfriendlyshoppinglist.ui.deleteproducts.listadapter;

import android.annotation.SuppressLint;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.support.v7.widget.AppCompatCheckBox;
import android.view.View;
import android.widget.TextView;

import privacyfriendlyshoppinglist.secuso.org.privacyfriendlyshoppinglist.R;
import privacyfriendlyshoppinglist.secuso.org.privacyfriendlyshoppinglist.framework.context.AbstractInstanceFactory;
import privacyfriendlyshoppinglist.secuso.org.privacyfriendlyshoppinglist.framework.context.InstanceFactory;
import privacyfriendlyshoppinglist.secuso.org.privacyfriendlyshoppinglist.framework.ui.AbstractViewHolder;
import privacyfriendlyshoppinglist.secuso.org.privacyfriendlyshoppinglist.framework.utils.MessageUtils;
import privacyfriendlyshoppinglist.secuso.org.privacyfriendlyshoppinglist.logic.product.business.ProductService;
import privacyfriendlyshoppinglist.secuso.org.privacyfriendlyshoppinglist.logic.product.business.domain.ProductItem;
import privacyfriendlyshoppinglist.secuso.org.privacyfriendlyshoppinglist.ui.deleteproducts.ShareProductsCache;

public class ShareProductsItemViewHolder extends AbstractViewHolder<ProductItem, ShareProductsCache>
{
    private ShareProductItemCache shareProductItemCache;
    private ShareProductsCache cache;
    private ProductService productService;

    public ShareProductsItemViewHolder(final View parent, ShareProductsCache cache) {
        super(parent,cache);
        this.shareProductItemCache = new ShareProductItemCache(parent);
        this.cache = cache;

        AbstractInstanceFactory instanceFactory = new InstanceFactory(cache.getActivity());
        this.productService = (ProductService) instanceFactory.createInstance(ProductService.class);
    }

    public void processItem(ProductItem item)
    {
        shareProductItemCache.getProductNameTextView().setText(item.getProductName());
        shareProductItemCache.getProductQuantityTextView().setText(item.getQuantity());
        shareProductItemCache.getCheckBox().setChecked(item.isChecked());
        shareProductItemCache.getShareImageButton().setVisibility(View.VISIBLE);
        shareProductItemCache.getCheckBox().setVisibility(View.INVISIBLE);
        updateVisibilityFormat(item);

        shareProductItemCache.getProductCard().setAlpha(0.0f);
        shareProductItemCache.getShareImageButton().setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                item.setSelectedForDeletion(!item.isSelectedForDeletion());
                updateVisibilityFormat(item);
            }
        });

    }

    @SuppressLint("RestrictedApi")
    private void updateVisibilityFormat(ProductItem item) {
        Resources resources = shareProductItemCache.getProductCard().getContext().getResources();
        TextView productNameTextView = shareProductItemCache.getProductNameTextView();
        TextView productQuantityTextView = shareProductItemCache.getProductQuantityTextView();
        AppCompatCheckBox checkBox = (AppCompatCheckBox) shareProductItemCache.getCheckBox();
        if ( item.isSelectedForDeletion() ) {
            int transparent = resources.getColor(R.color.transparent);
            int grey = resources.getColor(R.color.middlegrey);

            shareProductItemCache.getProductCard().setCardBackgroundColor(transparent);
            productNameTextView.setTextColor(grey);
            productQuantityTextView.setTextColor(grey);
            checkBox.setSupportButtonTintList(ColorStateList.valueOf(grey));

            String product_name = item.getProductName();
            String product_detail = item.getDetailInfo(cache.getActivity());
            String share_str = "Product: " + product_name+"\n"+ product_detail;
//            String shareableText = productService.getSharableText(item);
            MessageUtils.shareText(cache.getActivity(), share_str, item.getProductName());
        } else {
            int white = resources.getColor(R.color.white);
            int black = resources.getColor(R.color.black);

            shareProductItemCache.getProductCard().setCardBackgroundColor(white);
            productNameTextView.setTextColor(black);
            productQuantityTextView.setTextColor(black);
            checkBox.setSupportButtonTintList(ColorStateList.valueOf(black));
        }
    }
}

