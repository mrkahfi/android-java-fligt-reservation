package com.arenatiket.android.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.arenatiket.android.R;
import com.arenatiket.android.api.JsonCache;
import com.arenatiket.android.model.Passanger;
import com.arenatiket.android.model.Traveller;
import com.arenatiket.android.utils.MyApplication;
import com.arenatiket.android.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link com.arenatiket.android.fragment.PassangerInfoFragment.OnNextClickedListener}
 * interface.
 */
public class PassangerInfoFragment extends Fragment implements View.OnFocusChangeListener, View.OnClickListener {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    private static final int BIRTH_DATE = 0;
    private static final int PASSPORT_DATE = 1;
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private OnNextClickedListener mListener;

    private EditText nameEditText;
    private EditText birthDateTexEditText;
    //    private EditText nationalityEditText;
    private TextView nameTextView;
    private TextView birthDateTextView;
    private TextView nationalityTextView;
    private LinearLayout mrLayout;
    private LinearLayout msLayout;
    private LinearLayout mrsLayout;
    private TextView mrTextView;
    private TextView msTextView;
    private TextView mrsTextView;
    private int defaultColor;
    private LinearLayout nextButton;
    private String title = "1";
    private TextInputLayout nameLayout;
    private TextInputLayout birthDateLayout;
    private TextInputLayout nationalityLayout;
    private String name = "";
    private String birthDate = "";
    private String nationality = "";
    private String tabTitle;
    private int pageIndex;
    private boolean lastVisibility;
    private Passanger passanger;
    private TextInputLayout passportIdLayout;
    private TextView passportIdTextView;
    private TextView passportDateTextView;
    private TextInputLayout passportDateLayout;
    private TextView issuingCountryTextView;
    private TextInputLayout issuingCountryLayout;
    private EditText passportIdEditText;
    private EditText passportDateEditText;
    //    private EditText issuingCountryEditText;
    private AutoCompleteTextView nationalityAutoComplete;
    private ArrayList<String> nationalities;
    private ArrayAdapter<String> adapter;
    private AutoCompleteTextView issuingCountryAutoComplete;
    private String issuingCountry = "";
    private String passportId = "";
    private String passportDate = "";
    private String titleText = "Mr. ";
    private ArrayList<Traveller> travellers = new ArrayList<>();
    private LinearLayout titleLayout;
    private TextView idCardNumTextView;
    private TextInputLayout idCardNumLayout;
    private EditText idCardNumEditText;
    private String passangerType = "";
    private String idCardNum = "";
    private TextView ifantAssocTextView;
    private TextInputLayout ifantAssocLayout;
    private AutoCompleteTextView ifantAssocAutoComplete;
    private ArrayList<String> ifantAssocs = new ArrayList<>();
    private ArrayAdapter<String> ifantAssocAdapter;
    private int ifantAssoc;
    private LinearLayout mstrLayout, missLayout;
    private TextView missTextView, mstrTextView;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public PassangerInfoFragment() {
    }

    @SuppressWarnings("unused")
    public static PassangerInfoFragment newInstance(Passanger passanger, String passangerType, int pageIndex) {
        PassangerInfoFragment fragment = new PassangerInfoFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("passanger" + pageIndex, passanger);
        bundle.putString("passangerType", passangerType);
        bundle.putInt("pageIndex", pageIndex);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        Utils.logd("setUserVisibleHint");
        lastVisibility = isVisibleToUser;

        if (passangerType.equals("infant")) {
            titleText = "Mstr. ";
            title = "6";
            ifantAssocs.clear();
            for (Passanger passanger : MyApplication.passangers) {
                if (passanger.getType().equals("adult")) {
                    Utils.logd("passanger name : " + passanger.getName());
                    ifantAssocs.add(passanger.getName());
                }
            }
            ifantAssocAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, ifantAssocs);
            ifantAssocAdapter.notifyDataSetChanged();
        }

        if (passangerType.equals("child")) {
            titleText = "Mstr. ";
            title = "6";
        }

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
            pageIndex = getArguments().getInt("pageIndex");
            passanger = (Passanger) getArguments().getSerializable("passanger" + pageIndex);
            passangerType = getArguments().getString("passangerType");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_passanger_info, container, false);


        titleLayout = (LinearLayout) view.findViewById(R.id.titleLayout);
        mrLayout = (LinearLayout) view.findViewById(R.id.mrLayout);
        msLayout = (LinearLayout) view.findViewById(R.id.msLayout);
        mrsLayout = (LinearLayout) view.findViewById(R.id.mrsLayout);
        mstrLayout = (LinearLayout) view.findViewById(R.id.mstrLayout);
        missLayout = (LinearLayout) view.findViewById(R.id.missLayout);

        mrTextView = (TextView) view.findViewById(R.id.mrTextView);
        msTextView = (TextView) view.findViewById(R.id.msTextView);
        mrsTextView = (TextView) view.findViewById(R.id.mrsTextView);
        mstrTextView = (TextView) view.findViewById(R.id.mstrTextView);
        missTextView = (TextView) view.findViewById(R.id.missTextView);
        defaultColor = mrsTextView.getCurrentTextColor();

        nameTextView = (TextView) view.findViewById(R.id.nameTextView);
        birthDateTextView = (TextView) view.findViewById(R.id.birthDateTextView);
        idCardNumTextView = (TextView) view.findViewById(R.id.idCardNumTextView);
        ifantAssocTextView = (TextView) view.findViewById(R.id.ifantAssocTextView);
        nationalityTextView = (TextView) view.findViewById(R.id.nationalityTextView);
        passportIdTextView = (TextView) view.findViewById(R.id.passportIdTextView);
        passportDateTextView = (TextView) view.findViewById(R.id.passportDateTextView);
        issuingCountryTextView = (TextView) view.findViewById(R.id.issuingCountryTextView);

        nameLayout = (TextInputLayout) view.findViewById(R.id.nameLayout);
        birthDateLayout = (TextInputLayout) view.findViewById(R.id.birthDateLayout);
        idCardNumLayout = (TextInputLayout) view.findViewById(R.id.idCardNumLayout);
        ifantAssocLayout = (TextInputLayout) view.findViewById(R.id.ifantAssocLayout);
        nationalityLayout = (TextInputLayout) view.findViewById(R.id.nationalityLayout);
        passportIdLayout = (TextInputLayout) view.findViewById(R.id.passportIdLayout);
        passportDateLayout = (TextInputLayout) view.findViewById(R.id.passportDateLayout);
        issuingCountryLayout = (TextInputLayout) view.findViewById(R.id.issuingCountryLayout);

        nameEditText = (EditText) view.findViewById(R.id.nameEditText);
        birthDateTexEditText = (EditText) view.findViewById(R.id.birthDateEditText);
        idCardNumEditText = (EditText) view.findViewById(R.id.idCardNumEditText);
        ifantAssocAutoComplete = (AutoCompleteTextView) view.findViewById(R.id.ifantAssocAutoComplete);
//        nationalityEditText = (EditText) view.findViewById(R.id.nationalityEditText);
        nationalityAutoComplete = (AutoCompleteTextView) view.findViewById(R.id.nationalityAutoComplete);
        passportIdEditText = (EditText) view.findViewById(R.id.passportIdEditText);
        passportDateEditText = (EditText) view.findViewById(R.id.passportDateEditText);
//        issuingCountryEditText = (EditText) view.findViewById(R.id.issuingCountryEditText);
        issuingCountryAutoComplete = (AutoCompleteTextView) view.findViewById(R.id.issuingCountryAutoComplete);


        if (passangerType.equals("infant")) {
            ifantAssocs.clear();
            for (Passanger passanger : MyApplication.passangers) {
                Utils.logd("passanger name : " + passanger.getName());
                ifantAssocs.add(passanger.getName());
            }
            ifantAssocAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, ifantAssocs);
        }

        if (!passangerType.equals("adult")) {
            mrLayout.setVisibility(View.GONE);
            msLayout.setVisibility(View.GONE);
            mrsLayout.setVisibility(View.GONE);
            mstrLayout.setVisibility(View.VISIBLE);
            missLayout.setVisibility(View.VISIBLE);
        } else {
            mrLayout.setVisibility(View.VISIBLE);
            msLayout.setVisibility(View.VISIBLE);
            mrsLayout.setVisibility(View.VISIBLE);
            mstrLayout.setVisibility(View.GONE);
            missLayout.setVisibility(View.GONE);
        }

        ifantAssocAutoComplete.setAdapter(ifantAssocAdapter);

        Utils.logd(MyApplication.selectedDepartItem.getFlights().toString());
        try {
            if (MyApplication.isInternationalFlight) {
                checkInternationalAttribute();
            } else {
                checkAttribute();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        nextButton = (LinearLayout) view.findViewById(R.id.nextButton);

        initEventListener();

        nameEditText.setText(passanger.getName());
        nameEditText.setSelection(nameEditText.getText().length());

        nationalities = JsonCache.getNationalityNames(getActivity());

        adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, nationalities);
        nationalityAutoComplete.setAdapter(adapter);
        issuingCountryAutoComplete.setAdapter(adapter);

        return view;
    }

    private void checkAttribute() throws JSONException {
        String airline = MyApplication.selectedDepartItem.getFlights().getJSONObject(0).getString("airlines");
        travellers = JsonCache.getTravellers(getActivity());
        Traveller selectedTraveller = new Traveller();
        for (Traveller traveller : travellers) {
            if (traveller.getAirlines().equals(airline)) {
                selectedTraveller = traveller;
                break;
            }
        }
        JSONObject adultAttr = selectedTraveller.getAdultAttrJson();
        JSONObject childAttr = selectedTraveller.getChildAttrJson();
        JSONObject infantAttrJson = selectedTraveller.getInfantAttrJson();

        if (passangerType.equals("adult")) {
            for (Iterator<String> iter = adultAttr.keys(); iter.hasNext(); ) {
                String key = iter.next();
                JSONObject attr = adultAttr.getJSONObject(key);
                int isVisible = attr.getInt("is_required");
                int visible = isVisible == 1 ? View.VISIBLE : View.GONE;
                switch (key) {
                    case "title":
                        titleLayout.setVisibility(visible);
                        break;
                    case "first_name":
                        nameTextView.setVisibility(visible);
                        nameLayout.setVisibility(visible);
                        break;
                    case "last_name":
                        nameTextView.setVisibility(visible);
                        nameLayout.setVisibility(visible);
                        break;
                    case "date_of_birth":
                        birthDateTextView.setVisibility(visible);
                        birthDateLayout.setVisibility(visible);
                        break;
                    case "id_card_num":
                        idCardNumTextView.setVisibility(visible);
                        idCardNumLayout.setVisibility(visible);
                        break;
                    case "ifant_assoc_traveller_ctr":
                        break;
                    case "nationality_id":
                        nationalityTextView.setVisibility(visible);
                        nationalityLayout.setVisibility(visible);
                        break;
                    case "loyalty_id":
                        break;
                    case "passport_number":
                        passportIdTextView.setVisibility(visible);
                        passportIdLayout.setVisibility(visible);
                        break;
                    case "passport_expiry_date":
                        passportDateTextView.setVisibility(visible);
                        passportDateLayout.setVisibility(visible);
                        break;
                }
            }
        }

        if (passangerType.equals("child")) {
            for (Iterator<String> iter1 = childAttr.keys(); iter1.hasNext(); ) {
                String key1 = iter1.next();
                JSONObject attr1 = childAttr.getJSONObject(key1);
                int isVisible1 = attr1.getInt("is_required");
                int visible1 = isVisible1 == 1 ? View.VISIBLE : View.GONE;
                switch (key1) {
                    case "title":
                        titleLayout.setVisibility(visible1);
                        break;
                    case "first_name":
                        nameTextView.setVisibility(visible1);
                        nameLayout.setVisibility(visible1);
                        break;
                    case "last_name":
                        nameTextView.setVisibility(visible1);
                        nameLayout.setVisibility(visible1);
                        break;
                    case "date_of_birth":
                        birthDateTextView.setVisibility(visible1);
                        birthDateLayout.setVisibility(visible1);
                        break;
                    case "id_card_num":
                        idCardNumTextView.setVisibility(visible1);
                        idCardNumLayout.setVisibility(visible1);
                        break;
                    case "ifant_assoc_traveller_ctr":
                        break;
                    case "nationality_id":
                        nationalityTextView.setVisibility(visible1);
                        nationalityLayout.setVisibility(visible1);
                        break;
                    case "loyalty_id":
                        break;
                    case "passport_number":
                        passportIdTextView.setVisibility(visible1);
                        passportIdLayout.setVisibility(visible1);
                        break;
                    case "passport_expiry_date":
                        passportDateTextView.setVisibility(visible1);
                        passportDateLayout.setVisibility(visible1);
                        break;
                }
            }
        }

        if (passangerType.equals("infant")) {
            for (Iterator<String> iter2 = infantAttrJson.keys(); iter2.hasNext(); ) {
                String key2 = iter2.next();
                JSONObject attr2 = infantAttrJson.getJSONObject(key2);
                int isVisible2 = attr2.getInt("is_required");
                int visible2 = isVisible2 == 1 ? View.VISIBLE : View.GONE;
                switch (key2) {
                    case "title":
                        titleLayout.setVisibility(visible2);
                        break;
                    case "first_name":
                        nameTextView.setVisibility(visible2);
                        nameLayout.setVisibility(visible2);
                        break;
                    case "last_name":
                        nameTextView.setVisibility(visible2);
                        nameLayout.setVisibility(visible2);
                        break;
                    case "date_of_birth":
                        birthDateTextView.setVisibility(visible2);
                        birthDateLayout.setVisibility(visible2);
                        break;
                    case "id_card_num":
                        idCardNumTextView.setVisibility(visible2);
                        idCardNumLayout.setVisibility(visible2);
                        break;
                    case "ifant_assoc_traveller_ctr":
                        ifantAssocLayout.setVisibility(visible2);
                        ifantAssocTextView.setVisibility(visible2);
                        break;
                    case "nationality_id":
                        nationalityTextView.setVisibility(visible2);
                        nationalityLayout.setVisibility(visible2);
                        break;
                    case "loyalty_id":
                        break;
                    case "passport_number":
                        passportIdTextView.setVisibility(visible2);
                        passportIdLayout.setVisibility(visible2);
                        break;
                    case "passport_expiry_date":
                        passportDateTextView.setVisibility(visible2);
                        passportDateLayout.setVisibility(visible2);
                        break;
                }
            }
        }

    }


    private void checkInternationalAttribute() throws JSONException {
        String airline = MyApplication.selectedDepartItem.getFlights().getJSONObject(0).getString("airlines");
        travellers = JsonCache.getIntTravellers(getActivity());
        Traveller selectedTraveller = new Traveller();
        for (Traveller traveller : travellers) {
            if (traveller.getAirlines().equals(airline)) {
                selectedTraveller = traveller;
                break;
            }
        }
        JSONObject adultAttr = selectedTraveller.getAdultAttrJson();
        JSONObject childAttr = selectedTraveller.getChildAttrJson();
        JSONObject infantAttrJson = selectedTraveller.getInfantAttrJson();

        if (passangerType.equals("adult")) {
            for (Iterator<String> iter = adultAttr.keys(); iter.hasNext(); ) {
                String key = iter.next();
                JSONObject attr = adultAttr.getJSONObject(key);
                int isVisible = attr.getInt("is_required");
                int visible = isVisible == 1 ? View.VISIBLE : View.GONE;
                switch (key) {
                    case "title":
                        titleLayout.setVisibility(visible);
                        break;
                    case "first_name":
                        nameTextView.setVisibility(visible);
                        nameLayout.setVisibility(visible);
                        break;
                    case "last_name":
                        nameTextView.setVisibility(visible);
                        nameLayout.setVisibility(visible);
                        break;
                    case "date_of_birth":
                        birthDateTextView.setVisibility(visible);
                        birthDateLayout.setVisibility(visible);
                        break;
                    case "id_card_num":
                        idCardNumTextView.setVisibility(visible);
                        idCardNumLayout.setVisibility(visible);
                        break;
                    case "ifant_assoc_traveller_ctr":
                        break;
                    case "nationality_id":
                        nationalityTextView.setVisibility(visible);
                        nationalityLayout.setVisibility(visible);
                        break;
                    case "loyalty_id":
                        break;
                    case "passport_number":
                        passportIdTextView.setVisibility(visible);
                        passportIdLayout.setVisibility(visible);
                        break;
                    case "passport_expiry_date":
                        passportDateTextView.setVisibility(visible);
                        passportDateLayout.setVisibility(visible);
                        break;
                }
            }
        }

        if (passangerType.equals("child")) {
            for (Iterator<String> iter1 = childAttr.keys(); iter1.hasNext(); ) {
                String key1 = iter1.next();
                JSONObject attr1 = childAttr.getJSONObject(key1);
                int isVisible1 = attr1.getInt("is_required");
                int visible1 = isVisible1 == 1 ? View.VISIBLE : View.GONE;
                switch (key1) {
                    case "title":
                        titleLayout.setVisibility(visible1);
                        break;
                    case "first_name":
                        nameTextView.setVisibility(visible1);
                        nameLayout.setVisibility(visible1);
                        break;
                    case "last_name":
                        nameTextView.setVisibility(visible1);
                        nameLayout.setVisibility(visible1);
                        break;
                    case "date_of_birth":
                        birthDateTextView.setVisibility(visible1);
                        birthDateLayout.setVisibility(visible1);
                        break;
                    case "id_card_num":
                        idCardNumTextView.setVisibility(visible1);
                        idCardNumLayout.setVisibility(visible1);
                        break;
                    case "ifant_assoc_traveller_ctr":
                        break;
                    case "nationality_id":
                        nationalityTextView.setVisibility(visible1);
                        nationalityLayout.setVisibility(visible1);
                        break;
                    case "loyalty_id":
                        break;
                    case "passport_number":
                        passportIdTextView.setVisibility(visible1);
                        passportIdLayout.setVisibility(visible1);
                        break;
                    case "passport_expiry_date":
                        passportDateTextView.setVisibility(visible1);
                        passportDateLayout.setVisibility(visible1);
                        break;
                }
            }
        }

        if (passangerType.equals("infant")) {
            for (Iterator<String> iter2 = infantAttrJson.keys(); iter2.hasNext(); ) {
                String key2 = iter2.next();
                JSONObject attr2 = infantAttrJson.getJSONObject(key2);
                int isVisible2 = attr2.getInt("is_required");
                int visible2 = isVisible2 == 1 ? View.VISIBLE : View.GONE;
                switch (key2) {
                    case "title":
                        titleLayout.setVisibility(visible2);
                        break;
                    case "first_name":
                        nameTextView.setVisibility(visible2);
                        nameLayout.setVisibility(visible2);
                        break;
                    case "last_name":
                        nameTextView.setVisibility(visible2);
                        nameLayout.setVisibility(visible2);
                        break;
                    case "date_of_birth":
                        birthDateTextView.setVisibility(visible2);
                        birthDateLayout.setVisibility(visible2);
                        break;
                    case "id_card_num":
                        idCardNumTextView.setVisibility(visible2);
                        idCardNumLayout.setVisibility(visible2);
                        break;
                    case "ifant_assoc_traveller_ctr":
                        ifantAssocLayout.setVisibility(visible2);
                        ifantAssocTextView.setVisibility(visible2);
                        break;
                    case "nationality_id":
                        nationalityTextView.setVisibility(visible2);
                        nationalityLayout.setVisibility(visible2);
                        break;
                    case "loyalty_id":
                        break;
                    case "passport_number":
                        passportIdTextView.setVisibility(visible2);
                        passportIdLayout.setVisibility(visible2);
                        break;
                    case "passport_expiry_date":
                        passportDateTextView.setVisibility(visible2);
                        passportDateLayout.setVisibility(visible2);
                        break;
                }
            }
        }

    }


    public boolean setPassangerValue() {

        if (passanger != null) {
            name = nameEditText.getText().toString().toString();
            birthDate = birthDateTexEditText.getText().toString();
            idCardNum = idCardNumEditText.getText().toString();
            passportId = passportIdEditText.getText().toString();
            passportDate = passportDateEditText.getText().toString();
            boolean valid = validate(name, birthDate, nationality, idCardNum, passportId, passportDate, issuingCountry);
            if (valid) {
                passanger.setTitle(title);
                passanger.setTitleText(titleText);
                passanger.setName(name);
                passanger.setBirthDate(birthDate);
                passanger.setIdCardNum(idCardNum);
                passanger.setNationality(nationality);
                passanger.setPassportId(passportId);
                passanger.setPassportDate(passportDate);
                passanger.setPassportIssuingCountry(issuingCountry);
                Utils.logd("ifantAssoc " + ifantAssoc);
                passanger.setIfantAssocTravellerCtr(ifantAssoc + "");
                MyApplication.passangers.set(pageIndex, passanger);
                Utils.logd("visible gone valid");
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof OnNextClickedListener) {
            mListener = (OnNextClickedListener) activity;
        } else {
            throw new RuntimeException(activity.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

//    @Override
//    public void onAttach(Context context) {
//    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    private void initEventListener() {

        ifantAssocAutoComplete.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    ifantAssocAutoComplete.showDropDown();
                }
            }
        });

        mrLayout.setOnClickListener((View.OnClickListener) this);
        msLayout.setOnClickListener((View.OnClickListener) this);
        mrsLayout.setOnClickListener((View.OnClickListener) this);
        mstrLayout.setOnClickListener((View.OnClickListener) this);
        missLayout.setOnClickListener((View.OnClickListener) this);
        nextButton.setOnClickListener((View.OnClickListener) this);

        nameEditText.setOnFocusChangeListener((View.OnFocusChangeListener) this);
        birthDateTexEditText.setOnFocusChangeListener((View.OnFocusChangeListener) this);
        birthDateTexEditText.setOnClickListener((View.OnClickListener) this);
//        nationalityEditText.setOnFocusChangeListener((View.OnFocusChangeListener) this);
        nationalityAutoComplete.setOnFocusChangeListener((View.OnFocusChangeListener) this);
        passportDateEditText.setOnFocusChangeListener((View.OnFocusChangeListener) this);
        passportIdEditText.setOnFocusChangeListener((View.OnFocusChangeListener) this);
        issuingCountryAutoComplete.setOnFocusChangeListener((View.OnFocusChangeListener) this);

        ifantAssocAutoComplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ifantAssoc = position + 1;
                Utils.logd("ifantAssoc " + ifantAssoc);
            }
        });

        nationalityAutoComplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Utils.logd("== " + nationalityAutoComplete.getText().toString());
                int selectedPos = JsonCache.getNationalityNames(getActivity()).indexOf(nationalityAutoComplete.getText().toString());
                Utils.logd(JsonCache.getNationalityNames(getActivity()).get(selectedPos));
                nationality = JsonCache.getNationalityIds(getActivity()).get(selectedPos);
                Utils.logd("nationality " + nationality);
            }
        });

        issuingCountryAutoComplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Utils.logd("== " + issuingCountryAutoComplete.getText().toString());
                int selectedPos = JsonCache.getNationalityNames(getActivity()).indexOf(issuingCountryAutoComplete.getText().toString());
                issuingCountry = JsonCache.getNationalityIds(getActivity()).get(selectedPos);
                Utils.logd("issuingCountry " + issuingCountry);
            }
        });

        nameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                tabTitle = editable.toString();
                mListener.onTitleChangeListener(tabTitle, pageIndex);

            }
        });
    }

    @Override
    public void onFocusChange(View view, boolean hasFocus) {

        nameTextView.setTextColor(getResources().getColor(R.color.gray13));
        birthDateTextView.setTextColor(getResources().getColor(R.color.gray13));
        nationalityTextView.setTextColor(getResources().getColor(R.color.gray13));
        passportIdTextView.setTextColor(getResources().getColor(R.color.gray13));
        passportDateTextView.setTextColor(getResources().getColor(R.color.gray13));
        issuingCountryTextView.setTextColor(getResources().getColor(R.color.gray13));

        if (hasFocus) {
            if (view.getId() == nameEditText.getId()) {
                nameTextView.setTextColor(getResources().getColor(R.color.colorPrimary));
            } else if (view.getId() == birthDateTexEditText.getId()) {
                birthDateTextView.setTextColor(getResources().getColor(R.color.colorPrimary));
                mode = BIRTH_DATE;
                showDatePicker();
            } else if (view.getId() == nationalityAutoComplete.getId()) {
                nationalityTextView.setTextColor(getResources().getColor(R.color.colorPrimary));
            } else if (view.getId() == passportIdEditText.getId()) {
                passportIdTextView.setTextColor(getResources().getColor(R.color.colorPrimary));
            } else if (view.getId() == passportDateEditText.getId()) {
                passportDateTextView.setTextColor(getResources().getColor(R.color.colorPrimary));
                mode = PASSPORT_DATE;
                showDatePicker();
            } else if (view.getId() == issuingCountryAutoComplete.getId()) {
                issuingCountryTextView.setTextColor(getResources().getColor(R.color.colorPrimary));
            } else if (view.getId() == ifantAssocAutoComplete.getId()) {

            }
        }

    }

    @Override
    public void onClick(View view) {
        if (view.getId() == mrLayout.getId() || view.getId() == msLayout.getId() || view.getId() == mrsLayout.getId()
                || view.getId() == mstrLayout.getId() || view.getId() == missLayout.getId()) {
            mrLayout.setBackgroundResource(0);
            msLayout.setBackgroundResource(0);
            mrsLayout.setBackgroundResource(0);
            mstrLayout.setBackgroundResource(0);
            missLayout.setBackgroundResource(0);

            mrTextView.setTextColor(defaultColor);
            msTextView.setTextColor(defaultColor);
            mrsTextView.setTextColor(defaultColor);
            mstrTextView.setTextColor(defaultColor);
            missTextView.setTextColor(defaultColor);

            if (view.getId() == mrLayout.getId()) {
                mrLayout.setBackgroundResource(R.drawable.bg_capsule_active);
                mrTextView.setTextColor(getResources().getColor(R.color.colorPrimary));
                title = "1";
                titleText = "Mr. ";
            } else if (view.getId() == msLayout.getId()) {
                msLayout.setBackgroundResource(R.drawable.bg_capsule_active);
                msTextView.setTextColor(getResources().getColor(R.color.colorPrimary));
                title = "5";
                titleText = "Ms. ";
            } else if (view.getId() == mrsLayout.getId()) {
                mrsLayout.setBackgroundResource(R.drawable.bg_capsule_active);
                mrsTextView.setTextColor(getResources().getColor(R.color.colorPrimary));
                title = "2";
                titleText = "Mrs. ";
            } else if (view.getId() == mstrLayout.getId()) {
                mstrLayout.setBackgroundResource(R.drawable.bg_capsule_active);
                mstrTextView.setTextColor(getResources().getColor(R.color.colorPrimary));
                title = "6";
                titleText = "Mstr. ";
            } else if (view.getId() == missLayout.getId()) {
                missLayout.setBackgroundResource(R.drawable.bg_capsule_active);
                mstrTextView.setTextColor(getResources().getColor(R.color.colorPrimary));
                title = "3";
                titleText = "Miss. ";
            }
        } else if (view.getId() == nextButton.getId()) {
            boolean valid = setPassangerValue();
            if (valid) {
                mListener.onNextClickedListener();
            }
        } else if (view.getId() == birthDateTexEditText.getId()) {
            showDatePicker();
        }

    }

    private boolean validate(String name, String birthDate, String nationality, String idCardNum, String passportId,
                             String passportDate, String issuingCountry) {
        boolean valid = true;
        if ((name.isEmpty() || name.length() < 3) && nameLayout.getVisibility() == View.VISIBLE) {
            nameEditText.setError("Minimum 3 karakter");
            valid = false;
        } else {
            nameEditText.setError(null);
        }

        if (birthDate.isEmpty() && birthDateLayout.getVisibility() == View.VISIBLE) {
            birthDateTexEditText.setError("Masukan tanggal yang valid");
            valid = false;
        } else {
            birthDateTexEditText.setError(null);
        }


        if (idCardNum.isEmpty() && idCardNumLayout.getVisibility() == View.VISIBLE) {
            idCardNumLayout.setError("Wajib diisi");
            valid = false;
        } else {
            nationalityAutoComplete.setError(null);
        }


        if (nationality.isEmpty() && nationalityLayout.getVisibility() == View.VISIBLE) {
            nationalityAutoComplete.setError("Wajib diisi");
            valid = false;
        } else {
            nationalityAutoComplete.setError(null);
        }

        if (passportId.isEmpty() && passportIdLayout.getVisibility() == View.VISIBLE) {
            passportIdLayout.setError("Masukan tanggal yang valid");
            valid = false;
        } else {
            passportDateLayout.setError(null);
        }

        if (passportDate.isEmpty() && passportDateLayout.getVisibility() == View.VISIBLE) {
            passportDateLayout.setError("Masukan tanggal yang valid");
            valid = false;
        } else {
            passportDateLayout.setError(null);
        }

        if (issuingCountry.isEmpty() && issuingCountryLayout.getVisibility() == View.VISIBLE) {
            issuingCountryLayout.setError("Wajib diisi");
            valid = false;
        } else {
            issuingCountryLayout.setError(null);
        }

        MyApplication.isPassengerValid = valid;

        return valid;
    }

    private void showDatePicker() {
        DatePickerFragment date = new DatePickerFragment();
        /**
         * Set Up Current Date Into dialog
         */
        Calendar calender = Calendar.getInstance();
        Bundle args = new Bundle();
        args.putInt("year", calender.get(Calendar.YEAR));
        args.putInt("month", calender.get(Calendar.MONTH));
        args.putInt("day", calender.get(Calendar.DAY_OF_MONTH));
        date.setArguments(args);
        /**
         * Set Call back to capture selected date
         */
        date.setCallBack(ondate);
        date.show(getFragmentManager(), "Tanggal lahir");
    }

    private int mode;
    android.app.DatePickerDialog.OnDateSetListener ondate = new android.app.DatePickerDialog.OnDateSetListener() {

        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {

            if (mode == BIRTH_DATE) {
                birthDateTexEditText.setText(String.valueOf(dayOfMonth) + "-" + String.valueOf(monthOfYear + 1)
                        + "-" + String.valueOf(year));
            } else {
                passportDateEditText.setText(String.valueOf(dayOfMonth) + "-" + String.valueOf(monthOfYear + 1)
                        + "-" + String.valueOf(year));
            }
        }
    };

    public interface OnNextClickedListener {
        // TODO: Update argument type and name
        void onNextClickedListener();

        void onTitleChangeListener(String tabTitle, int pageIndex);
    }
}
