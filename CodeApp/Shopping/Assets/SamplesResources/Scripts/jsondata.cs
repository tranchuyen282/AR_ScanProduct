using System;
using System.Collections.Generic;
using UnityEngine;
using System.Collections;
using System.Collections.Generic;
using System.IO;
using System.Globalization;

public class jsondata : MonoBehaviour
{

    public ListProduct listProduct = new ListProduct();
   
    #region PROPERTIES
    string Title { get; set; }
    int Stores { get; set; }
    string Store1 { get; set; }
    string Store2 { get; set; }
    string Store3 { get; set; }
    string Best_Store { get; set; }
    int Best_Price { get; set; }
    string UrlBestStore { get; set; }
    string BrowserURL { get; set; }
    #endregion // PROPERTIES
    void Start()
    {
        //string path = Application.streamingAssetsPath + "/" + "test.json";
        //string jsonText = System.IO.File.ReadAllText(path); // read file to text
        TextAsset asset = Resources.Load("test") as TextAsset;
        listProduct = JsonUtility.FromJson<ListProduct>(asset.text);
        int i = 0;
        Product p = listProduct.Products[1];
        Debug.Log(p.uses);
       
    }
        

    //    char[] trimChars = { '{', '}', ' ', '[', ']',',' };
    //    jsonText = jsonText.Trim().Trim(trimChars).Trim();  // remove trimchars

    //    string[] products = jsonText.Split('}');            // split each product
    //    foreach (string oneProduct in products)
    //    {
    //        string product = oneProduct.Trim(trimChars).Trim().Trim(trimChars);   // get 1 product
    //        string[] jsonPairs = product.Split(',');                                  // split line
    //        foreach(string s in jsonPairs)
    //        {
    //            if (s.Contains(":"))                       
    //            {
    //                string[] keyValuePair = s.Split(new char[] { ':' }, 2);
    //                keyValuePair[0] = keyValuePair[0].Trim();
    //                keyValuePair[1] = keyValuePair[1].Trim();
    //                if (keyValuePair.Length == 2)
    //                {
    //                    switch (keyValuePair[0])
    //                    {

    //                        case "id":
                                
    //                            break;
    //                        case "engName":
                                
    //                            break;
    //                        case "bard":
                                
    //                            break;
    //                        case "size":
                               
    //                            break;
    //                        case "price":
                                
    //                            break;
    //                        case "exp":
                                
    //                            break;
    //                        case "uses":
                                
    //                            break;
    //                        case "manuals":
                               
    //                            break;
    //                        case "origin":
                                
    //                            break;
    //                        case "urlInfo":

    //                            break;
    //                        case "publish":

    //                            break;
    //                    }
    //                }

    //            }
    //            else
    //            {

    //            }
    //        }

    //    }
    //    //jsonText = jsonText.Trim(trimChars);

    //}



}
