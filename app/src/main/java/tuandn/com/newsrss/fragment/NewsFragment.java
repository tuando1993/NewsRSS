package tuandn.com.newsrss.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import de.greenrobot.event.EventBus;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;
import retrofit.SimpleXmlConverterFactory;
import tuandn.com.newsrss.DataForNewsPaper;
import tuandn.com.newsrss.MainActivity;
import tuandn.com.newsrss.R;
import tuandn.com.newsrss.vnexpress.ApiInterface;
import tuandn.com.newsrss.vnexpress.Rss;

/**
 * Created by Anh Trung on 11/9/2015.
 */
public class NewsFragment extends Fragment {

    public static final int NEWEST = 0;
    public static final int HEADLINE = 1;
    public static final int WORLD = 2;
    public static final int BUSINESS = 3;

    private static NewsFragment instance;
    private String[] news;
    private int position;
    private Call<Rss> call;

    private ListNewsAdapter mAdapter;
    private ListView mLvNew;
    private Rss rss;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.layout_news, container, false);
        mLvNew = (ListView) rootView.findViewById(R.id.lvListNew);

        rss = new Rss();


        DataForNewsPaper dataForNewsPaper = DataForNewsPaper.getInstance();
        String newspaperName = dataForNewsPaper.getNewspaperName();

        position = getArguments().getInt("position");


        if(newspaperName.equals(MainActivity.VNEXPRESS)) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("http://vnexpress.net")
                    .addConverterFactory(SimpleXmlConverterFactory.create())
                    .build();
            ApiInterface api = retrofit.create(ApiInterface.class);
            switch (position) {
                case NEWEST:
                    call = api.getNewestRssVnexpress();
                    break;
                case HEADLINE:
                    call = api.getHeadlineRssVnexpress();
                    break;
                case WORLD:
                    call = api.getWorldRssVnexpress();
                    break;
                case BUSINESS:
                    call = api.getBussinessRssVnexpress();
                    break;
                default:
                    call = api.getNewestRssVnexpress();
                    break;
            }

        } else if(newspaperName.equals(MainActivity.DANTRI)){
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("http://dantri.com.vn")
                    .addConverterFactory(SimpleXmlConverterFactory.create())
                    .build();
            ApiInterface api = retrofit.create(ApiInterface.class);
            switch (position) {
                case NEWEST:
                    call = api.getTrangchuDantri();
                    break;
                case HEADLINE:
                    call = api.getSuckhoeDantri();
                    break;
                case WORLD:
                    call = api.getXahoiDantri();
                    break;
                case BUSINESS:
                    call = api.getGiaitriDantri();
                    break;
                case 4:
                    call = api.getGiaoducDantri();
                    break;
                case 5:
                    call = api.getTheothaoDantri();
                    break;
                case 6:
                    call = api.getThegioiDantri();
                    break;
                case 7:
                    call = api.getKinhdoanhDantri();
                    break;
                default:
                    call = api.getTrangchuDantri();
                    break;
            }
        } else if(newspaperName.equals(MainActivity.ONLINE24H)){

        } else if(newspaperName.equals(MainActivity.BONGDAPLUS)){

        }


        // Asynchronous
        call.enqueue(new Callback<Rss>() {

            @Override
            public void onResponse(Response<Rss> response, Retrofit retrofit) {
                if (response.isSuccess()) {
//                    EventBus.getDefault().post(response.body());
                    mAdapter = new ListNewsAdapter(getContext(),response.body());
                    mLvNew.setAdapter(mAdapter);
                } else {
                }
            }

            @Override
            public void onFailure(Throwable t) {
                System.out.println("Loi: " + t.getMessage());
            }
        });

        rss = new Rss();
        mAdapter = new ListNewsAdapter(getContext(),rss);
        mLvNew.setAdapter(mAdapter);

        return rootView;
    }

    public static NewsFragment newInstance(int position) {
        Bundle args = new Bundle();
        args.putInt("position", position);
        NewsFragment fragment = new NewsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    public void onEvent(Rss response) {
        mAdapter = new ListNewsAdapter(getContext(),response);
        rss = new Rss();
        rss = response;
        mLvNew.setAdapter(mAdapter);
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }
}
