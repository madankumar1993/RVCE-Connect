/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: IJWSAndroidRemoteServiceCallback.aidl
 */
package com.rvceconnect.lib;
import java.lang.String;
import android.os.RemoteException;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Binder;
import android.os.Parcel;
public interface IJWSAndroidRemoteServiceCallback extends android.os.IInterface
{
/** Local-side IPC implementation stub class. */
public static abstract class Stub extends android.os.Binder implements com.rvceconnect.lib.IJWSAndroidRemoteServiceCallback
{
private static final java.lang.String DESCRIPTOR = "com.rvceconnect.lib.IJWSAndroidRemoteServiceCallback";
/** Construct the stub at attach it to the interface. */
public Stub()
{
this.attachInterface(this, DESCRIPTOR);
}
/**
 * Cast an IBinder object into an IJWSAndroidRemoteServiceCallback interface,
 * generating a proxy if needed.
 */
public static com.rvceconnect.lib.IJWSAndroidRemoteServiceCallback asInterface(android.os.IBinder obj)
{
if ((obj==null)) {
return null;
}
android.os.IInterface iin = (android.os.IInterface)obj.queryLocalInterface(DESCRIPTOR);
if (((iin!=null)&&(iin instanceof com.rvceconnect.lib.IJWSAndroidRemoteServiceCallback))) {
return ((com.rvceconnect.lib.IJWSAndroidRemoteServiceCallback)iin);
}
return new com.rvceconnect.lib.IJWSAndroidRemoteServiceCallback.Stub.Proxy(obj);
}
public android.os.IBinder asBinder()
{
return this;
}
@Override public boolean onTransact(int code, android.os.Parcel data, android.os.Parcel reply, int flags) throws android.os.RemoteException
{
switch (code)
{
case INTERFACE_TRANSACTION:
{
reply.writeString(DESCRIPTOR);
return true;
}
case TRANSACTION_onError:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
this.onError(_arg0);
reply.writeNoException();
return true;
}
}
return super.onTransact(code, data, reply, flags);
}
private static class Proxy implements com.rvceconnect.lib.IJWSAndroidRemoteServiceCallback
{
private android.os.IBinder mRemote;
Proxy(android.os.IBinder remote)
{
mRemote = remote;
}
public android.os.IBinder asBinder()
{
return mRemote;
}
public java.lang.String getInterfaceDescriptor()
{
return DESCRIPTOR;
}
public void onError(java.lang.String error) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(error);
mRemote.transact(Stub.TRANSACTION_onError, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
}
static final int TRANSACTION_onError = (IBinder.FIRST_CALL_TRANSACTION + 0);
}
public void onError(java.lang.String error) throws android.os.RemoteException;
}
