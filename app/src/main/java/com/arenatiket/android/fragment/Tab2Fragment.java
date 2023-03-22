package com.arenatiket.android.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.arenatiket.android.R;
import com.arenatiket.android.customlayout.CustomRangeBar;
import com.arenatiket.android.model.Airline;
import com.arenatiket.android.utils.Constants;
import com.arenatiket.android.utils.Filter;
import com.arenatiket.android.utils.Utils;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by kahfi on 21-01-2015.
 */
public class Tab2Fragment extends AppCompatDialogFragment {

    private RelativeLayout airlineFilterChoiceLayout;
    private RelativeLayout classFilterChoiceLayout;
    private RelativeLayout transitFilterChoiceLayout;
    private RelativeLayout directFlightFilterChoiceLayout;
    private TextView selectedFlightSubtitleTextView;
    private ArrayList<String> mSelectedTransitItems;
    private ArrayList<String> mSelectedTransitItemCodes;
    private CheckBox directFlightFilterCheckbox;

    private boolean directFlight;
    private ArrayList<String> choiceItems;
    private ArrayList<String> choiceItemCodes;
    private ArrayList<String> mSelectedClassItems;
    private ArrayList<String> mSelectedClassItemCodes;
    private TextView minPriceTextView;
    private TextView maxPriceTextView;
    private CustomRangeBar rangebar;
    private int minPrice;
    private int maxPrice;
    private LinearLayout priceRangeLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_tab2, container, false);
        airlineFilterChoiceLayout = (RelativeLayout) v.findViewById(R.id.airlineFilterChoiceLayout);
        selectedFlightSubtitleTextView = (TextView) v.findViewById(R.id.selectedFlightSubtitleTextView);
        classFilterChoiceLayout = (RelativeLayout) v.findViewById(R.id.classFilterChoiceLayout);
        transitFilterChoiceLayout = (RelativeLayout) v.findViewById(R.id.transitFilterChoiceLayout);
        directFlightFilterChoiceLayout = (RelativeLayout) v.findViewById(R.id.directFlightFilterChoiceLayout);
        directFlightFilterCheckbox = (CheckBox) v.findViewById(R.id.directFlightFilterCheckbox);
        priceRangeLayout = (LinearLayout) v.findViewById(R.id.priceRangeLayout);
        minPriceTextView = (TextView) v.findViewById(R.id.minPriceTextView);
        maxPriceTextView = (TextView) v.findViewById(R.id.maxPriceTextView);
        rangebar = (CustomRangeBar) v.findViewById(R.id.rangebar);

        String selectedText = "";
        int i = 0;
        for (String airline : Filter.mSelectedFlightItems) {
            selectedText = selectedText + ", " + airline;
            if (i == 0) selectedText = airline;
            i++;
        }
        selectedFlightSubtitleTextView.setText(selectedText);

        directFlightFilterCheckbox.setChecked(Filter.directFlight);

        airlineFilterChoiceLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                choiceItems = new ArrayList<String>();
                choiceItemCodes = new ArrayList<String>();
                for (Map.Entry<String, Airline> entry : Constants.airlines.entrySet()) {
                    String key = entry.getKey();
                    Airline value = entry.getValue();
                    choiceItemCodes.add(key);
                    choiceItems.add(value.getName());
                }

                CharSequence[] choiceItemsAray = new String[choiceItems.size()];
                choiceItemsAray = choiceItems.toArray(choiceItemsAray);

                boolean[] checkedItems = new boolean[choiceItems.size()];
                for (int i = 0; i < checkedItems.length; i++) {
                    Utils.logd("i " + i);
                    String airline = choiceItemCodes.get(i);
                    if (Filter.mSelectedFlightItemCodes.contains(airline)) {
                        checkedItems[i] = true;
                    } else {
                        checkedItems[i] = false;
                    }
                }
                builder.setTitle("Maskapai")
                        .setMultiChoiceItems(choiceItemsAray, checkedItems,
                                new DialogInterface.OnMultiChoiceClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which,
                                                        boolean isChecked) {
                                        Utils.logd("checked");
                                        if (isChecked) {
                                            if (!Filter.mSelectedFlightItems.contains(choiceItems.get(which))) {
                                                Utils.logd("add");
                                                Filter.mSelectedFlightItems.add(choiceItems.get(which));
                                                Filter.mSelectedFlightItemCodes.add(choiceItemCodes.get(which));
                                            }
                                        } else if (Filter.mSelectedFlightItems.contains(choiceItems.get(which))) {
                                            Utils.logd("remove");
                                            Filter.mSelectedFlightItems.remove(choiceItems.get(which));
                                            Filter.mSelectedFlightItemCodes.remove(choiceItemCodes.get(which));
                                            Utils.logd("Filter.mSelectedFlightItemCodes " + Filter.mSelectedFlightItems.size());
                                        }
                                    }
                                })
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                String selectedText = "";
                                int i = 0;
                                for (String airline : Filter.mSelectedFlightItems) {
                                    selectedText = selectedText + ", " + airline;
                                    if (i == 0) selectedText = airline;
                                    i++;
                                }
                                selectedFlightSubtitleTextView.setText(selectedText);
                            }
                        })
                        .setNegativeButton("Batal", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                            }
                        });
                builder.create().show();
            }
        });

        classFilterChoiceLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                choiceItems = new ArrayList<String>();
                choiceItemCodes = new ArrayList<String>();
                for (Map.Entry<String, String> entry : Constants.classes.entrySet()) {
                    String key = entry.getKey();
                    String value = entry.getValue();
                    choiceItemCodes.add(key);
                    choiceItems.add(value);
                }

                CharSequence[] choiceItemsAray = new String[choiceItems.size()];
                choiceItemsAray = choiceItems.toArray(choiceItemsAray);

                boolean[] checkedItems = new boolean[choiceItems.size()];
                for (int i = 0; i < checkedItems.length; i++) {
                    Utils.logd("i " + i);
                    String airline = choiceItemCodes.get(i);
                    if (Filter.mSelectedClassItemCodes.contains(airline)) {
                        checkedItems[i] = true;
                    } else {
                        checkedItems[i] = false;
                    }
                }
                builder.setTitle("Kelas")
                        .setMultiChoiceItems(choiceItemsAray, checkedItems,
                                new DialogInterface.OnMultiChoiceClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which,
                                                        boolean isChecked) {
                                        Utils.logd("checked");
                                        if (isChecked) {
                                            if (!Filter.mSelectedClassItems.contains(choiceItems.get(which))) {
                                                Utils.logd("add");
                                                Filter.mSelectedClassItems.add(choiceItems.get(which));
                                                Filter.mSelectedClassItemCodes.add(choiceItemCodes.get(which));
                                            }
                                        } else if (Filter.mSelectedClassItems.contains(choiceItems.get(which))) {
                                            Utils.logd("remove");
                                            Filter.mSelectedClassItems.remove(choiceItems.get(which));
                                            Filter.mSelectedClassItemCodes.remove(choiceItemCodes.get(which));
                                            Utils.logd("Filter.mSelectedClassItems " + Filter.mSelectedClassItems.size());
                                        }
                                    }
                                })
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                            }
                        })
                        .setNegativeButton("Batal", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                            }
                        });
                builder.create().show();
            }
        });

        transitFilterChoiceLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                choiceItems = new ArrayList<String>();
                choiceItemCodes = new ArrayList<String>();
                for (Map.Entry<String, String> entry : Constants.transits.entrySet()) {
                    String key = entry.getKey();
                    String value = entry.getValue();
                    choiceItemCodes.add(key);
                    choiceItems.add(value);
                }

                CharSequence[] choiceItemsAray = new String[choiceItems.size()];
                choiceItemsAray = choiceItems.toArray(choiceItemsAray);

                boolean[] checkedItems = new boolean[choiceItems.size()];
                for (int i = 0; i < checkedItems.length; i++) {
                    Utils.logd("i " + i);
                    String airline = choiceItemCodes.get(i);
                    if (Filter.mSelectedTransitItemCodes.contains(airline)) {
                        checkedItems[i] = true;
                    } else {
                        checkedItems[i] = false;
                    }
                }
                builder.setTitle("Transit")
                        .setMultiChoiceItems(choiceItemsAray, checkedItems,
                                new DialogInterface.OnMultiChoiceClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which,
                                                        boolean isChecked) {
                                        Utils.logd("checked");
                                        if (isChecked) {
                                            if (!Filter.mSelectedTransitItems.contains(choiceItems.get(which))) {
                                                Utils.logd("add");
                                                Filter.mSelectedTransitItems.add(choiceItems.get(which));
                                                Filter.mSelectedTransitItemCodes.add(choiceItemCodes.get(which));
                                            }
                                        } else if (Filter.mSelectedTransitItems.contains(choiceItems.get(which))) {
                                            Utils.logd("remove");
                                            Filter.mSelectedTransitItems.remove(choiceItems.get(which));
                                            Filter.mSelectedTransitItemCodes.remove(choiceItemCodes.get(which));
                                            Utils.logd("Filter.mSelectedTransitItems " + Filter.mSelectedTransitItems.size());
                                        }
                                    }
                                })
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                            }
                        })
                        .setNegativeButton("Batal", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                            }
                        });
                builder.create().show();
            }
        });

        directFlightFilterCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                Filter.directFlight = b;
            }
        });


        rangebar.setOnRangeBarChangeListener(new CustomRangeBar.OnRangeBarChangeListener() {

            @Override
            public void onRangeChangeListener(CustomRangeBar rangeBar, int leftPinIndex, int rightPinIndex,
                                              String leftPinValue, String rightPinValue) {
                Utils.logd("leftPinIndex " + leftPinIndex + ", rightPinIndex " + rightPinIndex + ", leftPinValue"
                        + leftPinValue + ", rightPinValue " + rightPinValue);
                Filter.minPrice = Integer.parseInt(leftPinValue.replace(".", "")) * 1000;
                Filter.maxPrice = Integer.parseInt(rightPinValue.replace(".", "")) * 1000;
                Filter.minIndex = leftPinIndex;
                Filter.maxIndex = rightPinIndex;
                minPriceTextView.setText(leftPinValue + ".000");
                maxPriceTextView.setText(rightPinValue + ".000");
            }
        });

        Utils.logd("Filter.minPrice " + Filter.minPrice + ", Filter.maxPrice " + Filter.maxPrice);

        if (Filter.minPrice != 0 && Filter.maxPrice != 0 && Filter.maxPrice - minPrice > 0) {
            rangebar.setTicks((float) minPrice, (float) maxPrice, 100000f);
        }
        if (Filter.minIndex != 0 && Filter.maxIndex != 0) {
            rangebar.setRangePinsByIndices(Filter.minIndex, Filter.maxIndex);
        }
        rangebar.setPinRadius(32f);

        minPriceTextView.setText(Utils.getMoneyFormat(Filter.minPrice, false));
        maxPriceTextView.setText(Utils.getMoneyFormat(Filter.maxPrice, false));

        directFlightFilterChoiceLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                directFlightFilterCheckbox.setChecked(!Filter.directFlight);
            }
        });

        return v;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        minPrice = getArguments().getInt("minPrice");
        maxPrice = getArguments().getInt("maxPrice");
    }

    public static Tab2Fragment newInstance(Bundle bundle) {
        Tab2Fragment fragment = new Tab2Fragment();
        fragment.setArguments(bundle);
        return fragment;
    }


    //    @Override
//    public Dialog onCreateDialog(Bundle savedInstanceState) {
//    }

}