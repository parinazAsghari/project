package com.example.parinaz.chainstoresapp;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.parinaz.chainstoresapp.object.MarkedProducts;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by parinaz on 7/22/19.
 */


public class MarkedProductsDBHelper extends SQLiteOpenHelper {


    public static String DB_NAME = "marked_products_db";
    public static int DB_VERSION = 1 ;
    public static String TABLE_NAME = "marked_products";
    public String command = "CREATE TABLE IF NOT EXISTS '" + TABLE_NAME + "'('code' int , 'name' varchar(128) , 'price' int ," +
            "'discount' int , 'reducedprice' int ,'category' int , 'image' varchar(128) , 'storeicon' varchar(128), 'storename' varchar(128),'branchid' int , 'stock' tinyint(1))";

    public MarkedProductsDBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(command);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
    public void insert (int code, String name, int price, int discount , int reducedPrice , int category, String image, String storeIcon,String storeName, int branchId , int stock){
        SQLiteDatabase db = getWritableDatabase();
        String query = "INSERT INTO '" + TABLE_NAME + "' ('code' , 'name' , 'price' , 'discount' , 'reducedprice' , 'category' , 'image' , 'storeicon' , 'storename', 'branchid' , 'stock') VALUES (" +
                code  + ",'" + name+"'," + price + "," + discount + "," + reducedPrice + "," + category + ",'" + image  + "', '" + storeIcon +"' , '" + storeName + "'," + branchId + "," + stock + ")";
        db.execSQL(query);
        Log.d("db insert" , name);
        if (db.isOpen()) db.close();
    }
    public void delete (int code , int branchId){
        SQLiteDatabase db = getReadableDatabase();
        db.delete(TABLE_NAME , "code =" + code + " AND branchid =" + branchId ,null);
        if (db.isOpen()) db.close();
    }
    public boolean isMarked (int code , int branchId){
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE code =" + code + " AND branchid =" + branchId ;
        Cursor cursor = db.rawQuery(query , null);
        if(cursor.getCount() <=0){
            cursor.close();
            if (db.isOpen()) db.close();
            return false;
        }
        cursor.close();
        if (db.isOpen()) db.close();
        return true;
    }
    public List<MarkedProducts> getAll (){
        List<MarkedProducts> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT * FROM '" + TABLE_NAME +"'" ;
        Cursor cursor = db.rawQuery(query , null);
        if(cursor.moveToFirst()){
            do{
                MarkedProducts p = new MarkedProducts();
                p.setCode(cursor.getInt(cursor.getColumnIndex("code")));
                p.setName(cursor.getString(cursor.getColumnIndex("name")));
                p.setPrice(cursor.getInt(cursor.getColumnIndex("price")));
                p.setDiscount(cursor.getInt(cursor.getColumnIndex("discount")));
                p.setReducedPrice(cursor.getInt(cursor.getColumnIndex("reducedprice")));
                p.setStoreBranchId(cursor.getInt(cursor.getColumnIndex("branchid")));
                p.setCategory(cursor.getInt(cursor.getColumnIndex("category")));
                p.setStock(cursor.getInt(cursor.getColumnIndex("stock")));
                p.setImage(cursor.getString(cursor.getColumnIndex("image")));
                p.setStoreIcon(cursor.getString(cursor.getColumnIndex("storeicon")));
                p.setStoreName(cursor.getString(cursor.getColumnIndex("storename")));
                list.add(p);
            }while (cursor.moveToNext());
        }
        cursor.close();
        if (db.isOpen()) db.close();
        return list;
    }




}
