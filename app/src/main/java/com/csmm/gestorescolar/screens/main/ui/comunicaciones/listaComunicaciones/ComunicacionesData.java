package com.csmm.gestorescolar.screens.main.ui.comunicaciones.listaComunicaciones;

public class ComunicacionesData {

    private String mSender;
    private String mAlumnoAsociado;
    private String mTitle;
    private String mDetails;
    private String mTime;
    private boolean mLeido;
    private boolean mImportante;

    public ComunicacionesData(String mSender, String mAlumnoAsociado, String mTitle, String mDetails, String mTime, boolean mLeido, boolean mImportante) {
        this.mSender = mSender;
        this.mAlumnoAsociado = mAlumnoAsociado;
        this.mTitle = mTitle;
        this.mDetails = mDetails;
        this.mTime = mTime;
        this.mLeido = mLeido;
        this.mImportante = mImportante;
    }

    public String getmSender() {
        return mSender;
    }

    public String getmAlumnoAsociado() {
        return mAlumnoAsociado;
    }

    public String getmTitle() {
        return mTitle;
    }

    public String getmDetails() {
        return mDetails;
    }

    public String getmTime() {
        return mTime;
    }

    public boolean getmLeido() {
        return mLeido;
    }

    public boolean getmImportante() {
        return mImportante;
    }
}