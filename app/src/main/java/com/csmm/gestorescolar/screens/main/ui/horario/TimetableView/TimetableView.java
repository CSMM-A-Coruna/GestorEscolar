package com.csmm.gestorescolar.screens.main.ui.horario.TimetableView;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.csmm.gestorescolar.R;

import java.util.ArrayList;
import java.util.List;

public class TimetableView extends LinearLayout {
    private static final int DEFAULT_ROW_COUNT = 12;
    private static final int DEFAULT_COLUMN_COUNT = 6;
    private static final int DEFAULT_CELL_HEIGHT_DP = 50;
    private static final int DEFAULT_SIDE_CELL_WIDTH_DP = 30;
    private static final int DEFAULT_START_TIME = 8;

    private static final int DEFAULT_SIDE_HEADER_FONT_SIZE_DP = 13;
    private static final int DEFAULT_HEADER_FONT_SIZE_DP = 15;
    private static final int DEFAULT_HEADER_HIGHLIGHT_FONT_SIZE_DP = 15;
    private static final int DEFAULT_STICKER_FONT_SIZE_DP = 13;


    private int rowCount;
    private int columnCount;
    private int cellHeight;
    private int sideCellWidth;
    private String[] headerTitle;
    private int startTime;
    private int headerHighlightColor;

    private RelativeLayout stickerBox;
    TableLayout tableHeader;
    TableLayout tableBox;

    private Context context;

    List<Sticker> stickers = new ArrayList<>();

    private OnStickerSelectedListener stickerSelectedListener = null;

    private HighlightMode highlightMode = HighlightMode.COLOR;
    private int headerHighlightImageSize;
    private Drawable headerHighlightImage = null;

    public TimetableView(Context context) {
        super(context, null);
        this.context = context;
    }

    public TimetableView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TimetableView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        getAttrs(attrs);
        init();
    }

    private void getAttrs(AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.TimetableView);
        rowCount = a.getInt(R.styleable.TimetableView_row_count, DEFAULT_ROW_COUNT) - 1;
        columnCount = a.getInt(R.styleable.TimetableView_column_count, DEFAULT_COLUMN_COUNT);
        cellHeight = a.getDimensionPixelSize(R.styleable.TimetableView_cell_height, dp2Px(DEFAULT_CELL_HEIGHT_DP));
        sideCellWidth = a.getDimensionPixelSize(R.styleable.TimetableView_side_cell_width, dp2Px(DEFAULT_SIDE_CELL_WIDTH_DP));
        int titlesId = a.getResourceId(R.styleable.TimetableView_header_title, R.array.default_header_title);
        headerTitle = a.getResources().getStringArray(titlesId);
        startTime = a.getInt(R.styleable.TimetableView_start_time, DEFAULT_START_TIME);
        headerHighlightColor = a.getColor(R.styleable.TimetableView_header_highlight_color, getContext().getColor(R.color.default_header_highlight_color));
        int highlightTypeValue = a.getInteger(R.styleable.TimetableView_header_highlight_type,0);
        if(highlightTypeValue == 0) highlightMode = HighlightMode.COLOR;
        else if(highlightTypeValue == 1) highlightMode = HighlightMode.IMAGE;
        headerHighlightImageSize = a.getDimensionPixelSize(R.styleable.TimetableView_header_highlight_image_size, dp2Px(24));
        headerHighlightImage = a.getDrawable(R.styleable.TimetableView_header_highlight_image);
        a.recycle();
    }

    private void init() {
        LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.view_timetable, this, false);
        addView(view);

        stickerBox = view.findViewById(R.id.sticker_box);
        tableHeader = view.findViewById(R.id.table_header);
        tableBox = view.findViewById(R.id.table_box);

        createTable();
    }

    public void setOnStickerSelectEventListener(OnStickerSelectedListener listener) {
        stickerSelectedListener = listener;
    }

    /**
     * date : 2019-02-08
     * get all schedules TimetableView has.
     */
    /*public ArrayList<Schedule> getAllSchedulesInStickers() {
        ArrayList<Schedule> allSchedules = new ArrayList<Schedule>();
        for (int key : stickers.keySet()) {
            for (Schedule schedule : stickers.get(key).getSchedules()) {
                allSchedules.add(schedule);
            }
        }
        return allSchedules;
    }*/

    /**
     * date : 2019-02-08
     * Used in Edit mode, To check a invalidate schedule.
     */
    /*public ArrayList<Schedule> getAllSchedulesInStickersExceptIdx(int idx) {
        ArrayList<Schedule> allSchedules = new ArrayList<Schedule>();
        for (int key : stickers.keySet()) {
            if (idx == key) continue;
            for (Schedule schedule : stickers.get(key).getSchedules()) {
                allSchedules.add(schedule);
            }
        }
        return allSchedules;
    }*/

    public void addSchedules(ArrayList<Schedule> schedules) {
        add(schedules);
    }

    private void add(final ArrayList<Schedule> schedules) {
        Sticker sticker = new Sticker();
        for (Schedule schedule : schedules) {
            TextView tv = new TextView(context);

            RelativeLayout.LayoutParams param = createStickerParam(schedule);

            tv.setLayoutParams(param);
            tv.setPadding(10, 0, 10, 0);
            tv.setText(schedule.getClassTitle());
            tv.setTextColor(Color.parseColor("#000000"));
            tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, DEFAULT_STICKER_FONT_SIZE_DP);
            //tv.setTypeface(null, Typeface.BOLD);
            tv.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            tv.setGravity(Gravity.CENTER);


            tv.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(stickerSelectedListener != null)
                        stickerSelectedListener.OnStickerSelected(schedule);
                }
            });

            sticker.addTextView(tv);
            sticker.addSchedule(schedule);
            sticker.addColor(schedule.getColor());
            sticker.getView().setBackgroundColor(Color.parseColor(schedule.getColor()));
            stickers.add(sticker);
            stickerBox.addView(tv);
        }
        //setStickerColor();
    }

    /*public String createSaveData() {
        return SaveManager.saveSticker(stickers);
    }*/

    /*public void load(String data) {
        removeAll();
        stickers = SaveManager.loadSticker(data);
        int maxKey = 0;
        for (int key : stickers.keySet()) {
            ArrayList<Schedule> schedules = stickers.get(key).getSchedules();
            add(schedules, key);
            if (maxKey < key) maxKey = key;
        }
        stickerCount = maxKey + 1;
        setStickerColor();
    }*/

    /*public void removeAll() {
        for (int key : stickers.keySet()) {
            Sticker sticker = stickers.get(key);
            for (TextView tv : sticker.getView()) {
                stickerBox.removeView(tv);
            }
        }
        stickers.clear();
    }*/

    /*public void edit(int idx, ArrayList<Schedule> schedules) {
        remove(idx);
        add(schedules, idx);
    }*/

    /*public void remove(int idx) {
        Sticker sticker = stickers.get(idx);
        for (TextView tv : sticker.getView()) {
            stickerBox.removeView(tv);
        }
        stickers.remove(idx);
        setStickerColor();
    }*/

    public void setHeaderHighlight(int idx) {
        if(idx < 0)return;
        TableRow row = (TableRow) tableHeader.getChildAt(0);
        View element = row.getChildAt(idx);
        if(highlightMode == HighlightMode.COLOR) {
            TextView tx = (TextView)element;
            tx.setTextColor(Color.parseColor("#FFFFFF"));
            tx.setBackgroundColor(headerHighlightColor);
            tx.setTypeface(null, Typeface.BOLD);
            tx.setTextSize(TypedValue.COMPLEX_UNIT_DIP, DEFAULT_HEADER_HIGHLIGHT_FONT_SIZE_DP);
        }
        else if(highlightMode == HighlightMode.IMAGE){
            RelativeLayout outer = new RelativeLayout(context);
            outer.setLayoutParams(createTableRowParam(cellHeight));
            ImageView iv = new ImageView(context);
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(headerHighlightImageSize,headerHighlightImageSize);
            params.addRule(RelativeLayout.CENTER_IN_PARENT,RelativeLayout.TRUE);
            iv.setLayoutParams(params);
            iv.setScaleType(ImageView.ScaleType.CENTER_CROP);

            row.removeViewAt(idx);
            outer.addView(iv);
            row.addView(outer,idx);

            if(headerHighlightImage != null) {
                iv.setImageDrawable(headerHighlightImage);
            }

        }
    }

    private void createTable() {
        createTableHeader();
        for (int i = 0; i < rowCount; i++) {
            TableRow tableRow = new TableRow(context);
            tableRow.setLayoutParams(createTableLayoutParam());

            for (int k = 0; k < columnCount; k++) {
                TextView tv = new TextView(context);
                tv.setLayoutParams(createTableRowParam(cellHeight));
                if (k == 0) {
                    tv.setText(getHeaderTime(i));
                    tv.setTextColor(getContext().getColor(R.color.colorHeaderText));
                    tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, DEFAULT_SIDE_HEADER_FONT_SIZE_DP);
                    tv.setBackgroundColor(getContext().getColor(R.color.colorHeader));
                    tv.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL);
                    tv.setPadding(0, -10, 0, 0);
                    tv.setLayoutParams(createTableRowParam(sideCellWidth, cellHeight));
                } else {
                    tv.setText("");
                    tv.setBackground(getContext().getDrawable(R.drawable.item_border));
                    tv.setGravity(Gravity.END);
                }
                tableRow.addView(tv);
            }
            tableBox.addView(tableRow);
        }
    }

    private void createTableHeader() {
        TableRow tableRow = new TableRow(context);
        tableRow.setLayoutParams(createTableLayoutParam());

        for (int i = 0; i < columnCount; i++) {
            TextView tv = new TextView(context);
            if (i == 0) {
                tv.setLayoutParams(createTableRowParam(sideCellWidth, cellHeight));
            } else {
                tv.setLayoutParams(createTableRowParam(cellHeight));
            }
            tv.setTextColor(getContext().getColor(R.color.colorHeaderText));
            tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, DEFAULT_HEADER_FONT_SIZE_DP);
            tv.setText(headerTitle[i]);
            tv.setGravity(Gravity.CENTER);

            tableRow.addView(tv);
        }
        tableHeader.addView(tableRow);
    }

    private RelativeLayout.LayoutParams createStickerParam(Schedule schedule) {
        int cell_w = calCellWidth();

        RelativeLayout.LayoutParams param = new RelativeLayout.LayoutParams(cell_w, calStickerHeightPx(schedule));
        param.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        param.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        param.setMargins(sideCellWidth + cell_w * schedule.getDay(), calStickerTopPxByTime(schedule.getStartTime()), 0, 0);

        return param;
    }

    private int calCellWidth(){
        Display display = ((Activity) context).getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int cell_w = (size.x-getPaddingLeft() - getPaddingRight()- sideCellWidth) / (columnCount - 1);
        return cell_w;
    }

    private int calStickerHeightPx(Schedule schedule) {
        int startTopPx = calStickerTopPxByTime(schedule.getStartTime());
        int endTopPx = calStickerTopPxByTime(schedule.getEndTime());
        int d = endTopPx - startTopPx;

        return d;
    }

    private int calStickerTopPxByTime(Time time) {
        return (time.getHour() - startTime) * cellHeight + (int) ((time.getMinute() / 60.0f) * cellHeight);
    }

    private TableLayout.LayoutParams createTableLayoutParam() {
        return new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.MATCH_PARENT);
    }

    private TableRow.LayoutParams createTableRowParam(int h_px) {
        return new TableRow.LayoutParams(calCellWidth(), h_px);
    }

    private TableRow.LayoutParams createTableRowParam(int w_px, int h_px) {
        return new TableRow.LayoutParams(w_px, h_px);
    }

    private String getHeaderTime(int i) {
        return String.valueOf(startTime + i);
    }

    static private int dp2Px(int dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }

    private void onCreateByBuilder(Builder builder) {
        this.rowCount = builder.rowCount;
        this.columnCount = builder.columnCount;
        this.cellHeight = builder.cellHeight;
        this.sideCellWidth = builder.sideCellWidth;
        this.headerTitle = builder.headerTitle;
        this.startTime = builder.startTime;
        this.headerHighlightColor = builder.headerHighlightColor;

        init();
    }


    public interface OnStickerSelectedListener {
        void OnStickerSelected(Schedule schedule);
    }

    static class Builder {
        private Context context;
        private int rowCount;
        private int columnCount;
        private int cellHeight;
        private int sideCellWidth;
        private String[] headerTitle;
        private String[] stickerColors;
        private int startTime;
        private int headerHighlightColor;

        public Builder(Context context) {
            this.context = context;
            rowCount = DEFAULT_ROW_COUNT;
            columnCount = DEFAULT_COLUMN_COUNT;
            cellHeight = dp2Px(DEFAULT_CELL_HEIGHT_DP);
            sideCellWidth = dp2Px(DEFAULT_SIDE_CELL_WIDTH_DP);
            headerTitle = context.getResources().getStringArray(R.array.default_header_title);
            stickerColors = context.getResources().getStringArray(R.array.default_sticker_color);
            startTime = DEFAULT_START_TIME;
            headerHighlightColor = context.getColor(R.color.default_header_highlight_color);
        }

        public Builder setRowCount(int n) {
            this.rowCount = n;
            return this;
        }

        public Builder setColumnCount(int n) {
            this.columnCount = n;
            return this;
        }

        public Builder setCellHeight(int dp) {
            this.cellHeight = dp2Px(dp);
            return this;
        }

        public Builder setSideCellWidth(int dp) {
            this.sideCellWidth = dp2Px(dp);
            return this;
        }

        public Builder setHeaderTitle(String[] titles) {
            this.headerTitle = titles;
            return this;
        }

        public Builder setStickerColors(String[] colors) {
            this.stickerColors = colors;
            return this;
        }

        public Builder setStartTime(int t) {
            this.startTime = t;
            return this;
        }

        public Builder setHeaderHighlightColor(int c) {
            this.headerHighlightColor = c;
            return this;
        }

        public TimetableView build() {
            TimetableView timetableView = new TimetableView(context);
            timetableView.onCreateByBuilder(this);
            return timetableView;
        }
    }
}