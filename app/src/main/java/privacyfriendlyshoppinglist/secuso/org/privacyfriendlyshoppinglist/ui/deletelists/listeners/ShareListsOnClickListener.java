package privacyfriendlyshoppinglist.secuso.org.privacyfriendlyshoppinglist.ui.deletelists.listeners;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import java.util.List;

import privacyfriendlyshoppinglist.secuso.org.privacyfriendlyshoppinglist.R;
import privacyfriendlyshoppinglist.secuso.org.privacyfriendlyshoppinglist.framework.context.AbstractInstanceFactory;
import privacyfriendlyshoppinglist.secuso.org.privacyfriendlyshoppinglist.framework.context.InstanceFactory;
import privacyfriendlyshoppinglist.secuso.org.privacyfriendlyshoppinglist.framework.utils.MessageUtils;
import privacyfriendlyshoppinglist.secuso.org.privacyfriendlyshoppinglist.framework.utils.NotificationUtils;
import privacyfriendlyshoppinglist.secuso.org.privacyfriendlyshoppinglist.logic.product.business.ProductService;
import privacyfriendlyshoppinglist.secuso.org.privacyfriendlyshoppinglist.logic.product.business.domain.ProductItem;
import privacyfriendlyshoppinglist.secuso.org.privacyfriendlyshoppinglist.logic.shoppingList.business.ShoppingListService;
import privacyfriendlyshoppinglist.secuso.org.privacyfriendlyshoppinglist.logic.shoppingList.business.domain.ListItem;
import privacyfriendlyshoppinglist.secuso.org.privacyfriendlyshoppinglist.ui.deletelists.DeleteListsCache;
import privacyfriendlyshoppinglist.secuso.org.privacyfriendlyshoppinglist.ui.deletelists.ShareListsCache;
import privacyfriendlyshoppinglist.secuso.org.privacyfriendlyshoppinglist.ui.main.MainActivity;
import privacyfriendlyshoppinglist.secuso.org.privacyfriendlyshoppinglist.ui.shoppinglist.reminder.ReminderReceiver;
import privacyfriendlyshoppinglist.secuso.org.privacyfriendlyshoppinglist.ui.shoppinglist.reminder.ReminderSchedulingService;
import rx.Observable;
import rx.schedulers.Schedulers;

public class ShareListsOnClickListener implements View.OnClickListener
{
    private ShoppingListService shoppingListService;
    private ProductService productService;
    private ShareListsCache cache;

    public ShareListsOnClickListener(ShareListsCache cache)
    {
        this.cache = cache;
        AbstractInstanceFactory instanceFactory = new InstanceFactory(cache.getActivity().getApplicationContext());
        this.shoppingListService = (ShoppingListService) instanceFactory.createInstance(ShoppingListService.class);
        this.productService = (ProductService) instanceFactory.createInstance(ProductService.class);
    }

    @Override
    public void onClick(View v)
    {
//        MessageUtils.showAlertDialog(cache.getActivity(), R.string.delete_confirmation_title, R.string.delete_lists_confirmation, shareLists());
        List<ListItem> shoppingList = cache.getShareListsAdapter().getList();
        Toast.makeText(cache.getActivity(), ""+shoppingList.get(0).getListName(), Toast.LENGTH_SHORT).show();
    }


    private Observable<Void> shareLists() {
        Observable observable = Observable
                .defer(() -> Observable.just(shareListsSync()))
                .doOnError(Throwable::printStackTrace)
                .subscribeOn(Schedulers.computation());
        return observable;
    }

    private Void shareListsSync()
    {
        List<ListItem> shoppingList = cache.getShareListsAdapter().getList();
        shoppingListService.deleteSelected(shoppingList)
                .doOnNext(id ->
                {
                    // delete all products
                    productService.deleteAllFromList(id).doOnError(Throwable::printStackTrace).subscribe();
//                    ProductItem item = productService.getAllProducts(id);
//                    String shareableText = productService.getSharableText(item);
//                    MessageUtils.shareText(cache.getActivity(), shareableText, item.getProductName());
                    // delete reminder
                    ReminderReceiver alarm = new ReminderReceiver();
                    Intent intent = new Intent(cache.getActivity(), ReminderSchedulingService.class);
                    alarm.cancelAlarm(cache.getActivity(), intent, id);
                    NotificationUtils.removeNotification(cache.getActivity(), id);
                })
                .doOnError(Throwable::printStackTrace)
                .subscribe();

        // go back to list overview
        AppCompatActivity activity = cache.getActivity();
        Intent intent = new Intent(activity, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        activity.startActivity(intent);
        return null;
    }
}
