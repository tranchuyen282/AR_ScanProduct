using System.Collections;
using System.Collections.Generic;
using System.Globalization;
using UnityEngine;
using UnityEngine.UI;
using System.IO;
using System.Globalization;

public class CloudContentManager : MonoBehaviour
{
    ListProduct Products;
    List<Product> listProduct = new List<Product>();
    Product product;

   
    #region PUBLIC_MEMBERS
  
    public Text m_engName;
    public Text m_vieName;
    public Text m_bard;
    public Text m_size;
    public Text m_price;
    public Text m_use1;
    public Text m_use2;
    public Text m_use3;


    #endregion PUBLIC_MEMBERS


    #region PRIVATE_MEMBERS
    bool wwwRequestInProgress;
    #endregion PRIVATE_MEMBERS


    void Start()
    {
        TextAsset asset = Resources.Load("product2") as TextAsset;
        Products = JsonUtility.FromJson<ListProduct>(asset.text);
        listProduct = Products.Products;    // save data from file to list
        Debug.Log("length of list" + string.Format(" {0} ", listProduct.Count));
    }


    #region MONOBEHAVIOUR_METHODS
    void Update()
    {
        
    }
    #endregion // MONOBEHAVIOUR_METHODS


    #region PUBLIC_METHODS
    public void HandleMetadata(string metadata)
    {
        // metadata string will be in the following format: name.json

        // string fullURL = JSON_URL + metadata;
        // StartCoroutine(WebRequest(fullURL));

        // Read data from list to show 
        string id = metadata.Split('.')[0];
        product = findInfoProduct(id);
        UpdateBookTextData();

    }

    public void ClearBookData()
    {
        m_engName.text = "KiotViet";
        m_bard.text = string.Format("KiotViet");
        m_use1.text = "KiotViet";
        m_use2.text = "KiotViet";
        m_use3.text = "KiotViet";
        m_price.text = string.Format("Best Price\n {0} VNĐ", 0);
        m_size.text = "Size";
    }
    #endregion // PUBLIC_METHODS


    #region BUTTON_METHODS
    public void MoreInfoButton()
    {
        Application.OpenURL(product.urlInfo);
    }
    #endregion // BUTTON_METHODS

    #region PRIVATE_METHODS

    void UpdateBookTextData()
    {
        Debug.Log("UpdateTextData() called.");
        //m_Title.text = Title;
        //m_Stores.text = string.Format("({0} stores)", Stores);
        //m_Store1.text = "1." + Store1 + " (3 Km)";
        //m_Store2.text = "2." + Store2 + " (4 Km)";
        //m_Store3.text = "3." + Store3 + " (4.5 Km)";
        //m_BestStore.text = "At " + Best_Store;
        //m_BestPrice.text = string.Format("Best Pirce\n {0} VNĐ", Best_Price);
        string[] use = product.uses.Split(';');

        m_engName.text = product.engName;
        m_bard.text = product.bard;
        m_use1.text = use[0];
        m_use2.text = use[1];
        m_use3.text = use[2];
        m_price.text = string.Format("Best Price\n {0} VNĐ", product.price);
        m_size.text = string.Format("Size: {0}",product.size);

    }

    void ProcessWebRequest(WWW www)
    {
        Debug.Log("ProcessWebRequest() called: \n" + www.url);

        if (www.url.Contains(".json"))
        {
            ParseJSON(www.text);
            www.Dispose();
            UpdateBookTextData();
        }
    }

    IEnumerator WebRequest(string url)
    {
        Debug.Log("WebRequest() called: \n" + url);

        wwwRequestInProgress = true;

        if (string.IsNullOrEmpty(url))
        {
            Debug.LogError("WebRequest() failed. Your URL is null or empty.");
            yield return null;
        }

        WWW www = new WWW(url);
        yield return www;

        if (www.isDone)
        {
            Debug.Log("Done Loading: \n" + www.url);
            wwwRequestInProgress = false;
        }

        if (string.IsNullOrEmpty(www.error))
        {
            // If error string is null or empty, then request was successful
            ProcessWebRequest(www);
        }
        else
        {
            Debug.LogError("Error With WWW Request: " + www.error);

            string error = "<color=red>" + www.error + "</color>" + "\nURL Requested: " + www.url;

            MessageBox.DisplayMessageBox("WWW Request Error", error, true, null);
        }
    }


    void ParseJSON(string jsonText)
    {
        Debug.Log("ParseJSON() called: \n" + jsonText);

        // Remove opening and closing braces and any spaces
        char[] trimChars = { '{', '}', ' ', '[', ']' };

        // Remove double quote and new line chars from the JSON text
        jsonText = jsonText.Trim(trimChars).Replace("\"", "").Replace("\n", "");
        //jsonText = jsonText.Trim(trimChars);
        string[] jsonPairs = jsonText.Split(',');

        Debug.Log("# of JSON pairs: " + jsonPairs.Length);

        foreach (string pair in jsonPairs)
        {
            // Split pair into a max of two strings using first colon
            string[] keyValuePair = pair.Split(new char[] { ':' }, 2);
            keyValuePair[0] = keyValuePair[0].Trim();
            keyValuePair[1] = keyValuePair[1].Trim();
            if (keyValuePair.Length == 2)
            {
                switch (keyValuePair[0])
                {

                    //case TITLE_KEY:
                    //    Title = keyValuePair[1];
                    //    break;
                    //case STORES:
                    //    Stores = int.Parse(keyValuePair[1], CultureInfo.InvariantCulture);
                    //    break;
                    //case STORE1_NAME:
                    //    Store1 = keyValuePair[1];
                    //    break;
                    //case STORE2_NAME:
                    //    Store2 = keyValuePair[1];
                    //    break;
                    //case STORE3_NAME:
                    //    Store3 = keyValuePair[1];
                    //    break;
                    //case BEST_PRICE:
                    //    Best_Price = int.Parse(keyValuePair[1], CultureInfo.InvariantCulture);
                    //    break;
                    //case BEST_STORE:
                    //    Best_Store = keyValuePair[1];
                    //    break;
                    //case URL_BEST_STORE:
                    //    UrlBestStore = keyValuePair[1];
                    //    break;
                    //case INFO_URL_KEY:
                    //    BrowserURL = keyValuePair[1];
                    //    break;
                }
            }
        }
    }



    Product findInfoProduct(string metaData)
    {
        foreach(Product p in listProduct)
        {
            if (p.id.CompareTo(metaData) == 0)
            {
                return p;
            }
        }
        return null;
    }
    #endregion //PRIVATE_METHODS
}
