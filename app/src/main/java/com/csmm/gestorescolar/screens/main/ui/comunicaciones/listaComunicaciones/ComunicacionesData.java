package com.csmm.gestorescolar.screens.main.ui.comunicaciones.listaComunicaciones;

public class ComunicacionesData {

    private String mRemite;
    private String mAlumnoAsociado;
    private String mAsunto;
    private String mCuerpo;
    private String mHora;
    private boolean mLeido;
    private boolean mImportante;

    public ComunicacionesData(String mRemite, String mAlumnoAsociado, String mAsunto, String mCuerpo, String mHora, boolean mLeido, boolean mImportante) {
        this.mRemite = mRemite;
        this.mAlumnoAsociado = mAlumnoAsociado;
        this.mAsunto = mAsunto;
        this.mCuerpo = mCuerpo;
        this.mHora = mHora;
        this.mLeido = mLeido;
        this.mImportante = mImportante;
    }

    public String getmRemite() {
        return mRemite;
    }

    public String getmAlumnoAsociado() {
        return mAlumnoAsociado;
    }

    public String getmAsunto() {
        return mAsunto;
    }

    public String getmCuerpo() {
        return mCuerpo;
    }

    public String getmHora() {
        return mHora;
    }

    public boolean getmLeido() {
        return mLeido;
    }

    public boolean getmImportante() {
        return mImportante;
    }
}