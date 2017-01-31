package tech.receipts.ui.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import mehdi.sakout.aboutpage.AboutPage;
import mehdi.sakout.aboutpage.Element;
import tech.receipts.BuildConfig;
import tech.receipts.R;
import timber.log.Timber;

public class AboutActivity extends BaseActivity {

    @BindString(R.string.about_application_version)
    protected String aboutApplicationVersion;

    @BindString(R.string.about_contact_us)
    protected String aboutContact;

    @BindString(R.string.about_connect_with_us)
    protected String aboutConnectWithUs;

    @BindString(R.string.about_open_source)
    protected String aboutOpenSource;

    @BindString(R.string.about_social)
    protected String aboutSocial;

    @BindString(R.string.title_section_about)
    protected String titleSectionAbout;

    @BindString(R.string.contact_email)
    protected String contactEmail;

    @BindString(R.string.about_facebook)
    protected String aboutFacebook;

    @BindString(R.string.about_twitter)
    protected String aboutTwitter;

    @BindString(R.string.about_first_paragraph)
    protected String aboutFirstParagraph;

    @BindString(R.string.about_second_paragraph)
    protected String aboutSecondParagraph;

    @BindString(R.string.about_third_paragraph)
    protected String aboutThirdParagraph;

    @BindString(R.string.about_fourth_paragraph)
    protected String aboutFourthParagraph;

    @BindString(R.string.about_fifth_paragraph)
    protected String aboutFifthParagraph;

    @BindString(R.string.about_sixth_paragraph)
    protected String aboutSixthParagraph;

    @BindView(R.id.toolbar)
    protected Toolbar toolbar;

    @BindView(R.id.about_view)
    protected LinearLayout linearLayout;

    private Unbinder unbinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        component().inject(this);
        setContentView(R.layout.activity_about);
        unbinder = ButterKnife.bind(this);

        Timber.d("onCreate: %s", savedInstanceState);

        setSupportActionBar(toolbar);
        ActionBar bar = getSupportActionBar();
        if (bar != null) {
            bar.setHomeButtonEnabled(true);
            bar.setDisplayHomeAsUpEnabled(true);
            bar.setDisplayShowTitleEnabled(true);
            bar.setTitle(titleSectionAbout);
        }

        String version = TextUtils.join(" ", new String[]{aboutApplicationVersion, BuildConfig.VERSION_NAME});
        String description = TextUtils.join("\n\n", new String[]{
                aboutFirstParagraph,
                aboutSecondParagraph,
                aboutThirdParagraph,
                aboutFourthParagraph,
                aboutFifthParagraph,
                aboutSixthParagraph
        });

        Element versionElement = new Element();
        versionElement.setTitle(version);

        View aboutView = new AboutPage(this)
                .setDescription(description)
                .isRTL(false)
//                .setImage(R.drawable.drawer_header)
                .addItem(versionElement)
                .addGroup(aboutConnectWithUs)
                .addEmail(contactEmail)
//                .addPlayStore("tech.receipts.android")
//                .addGroup(aboutOpenSource)
//                .addGitHub("receipts")
                .addGroup(aboutSocial)
                .addYoutube("UCqMYqLsLgXav19iEIaCfBwg")
                .addFacebook("Loteria-paragonowa-227884747588036")
                .addTwitter("mobulum_com")
                .create();

        linearLayout.addView(aboutView);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

}
