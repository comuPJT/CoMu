using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class SPUM_Exporter : MonoBehaviour
{
    public GameObject _unitPrefab;
	public string _imageName;
    public enum UnitType
    {
        SwordMan,
        BowMan,
        Magician
    }
    public UnitType _unitType = UnitType.SwordMan;
	public bool _separated = false; 
	public string _sepaName ="";
    public Vector2 _imageSize = new Vector2(128,128);
    public Vector2 _fullSize = new Vector2(1024,1024);
    public float _scaleFactor = 1;
    public int _frameRate = 8;
	public int _frameNumber = 0;
	public bool _advanced;

	int ImageNumber;

    // Start is called before the first frame update


    public void StartExport()
    {
		ImageNumber = 0;
        _animNameNow.Clear();
        _animNameNow.Add( _animNameList[0]);
        _animNameNow.Add( _animNameList[1]);
        _animNameNow.Add( _animNameList[2]);
        _animNameNow.Add( _animNameList[3]);

        switch(_unitType)
        {
            case UnitType.SwordMan:
            _animNameNow.Add( _animNameList[4]);
            _animNameNow.Add( _animNameList[7]);
            break;

            case UnitType.BowMan:
            _animNameNow.Add( _animNameList[5]);
            _animNameNow.Add( _animNameList[8]);
            break;

            case UnitType.Magician:
            _animNameNow.Add( _animNameList[6]);
            _animNameNow.Add( _animNameList[9]);
            break;
        }
    }

    public void CheckObjNow()
    {
        _objectNow = null;
        _anim = null;
        if(_objectPivot.childCount > 0)
        {
            DestroyImmediate(_objectPivot.GetChild(0).gameObject);
        }
    }

    public void MakeObjNow()
    {
        if(_objectNow!=null) return;

        GameObject tObj = Instantiate(_unitPrefab);
        tObj.transform.SetParent(_objectPivot);
        tObj.transform.localScale = new Vector3(1,1,1);
        tObj.transform.localPosition = new Vector3(0,-0.5f,0);

        _objectNow = tObj;
        _anim = tObj.transform.GetChild(0).GetComponent<Animator>();
    }

	//advanced field

	public Camera _camera;
	public Animator _anim;
	public Transform _objectPivot;
	public GameObject _objectNow;
	public RectTransform _imgBG;
	public GameObject _bgSet;
	public int frameNowNumber;
	public int _animNum;
	public float timer;
	public float timerForSave;
	public bool useTimer;
	public bool _netAnimClip;
	public int animNum;
    public AnimationClip[] animationClips;
	public List<string> _animNameList = new List<string>();
	public List<string> _animNameNow = new List<string>();
	public List<Texture2D> _textSaveList = new List<Texture2D>();
	public RenderTexture tempRT;
    // Start is called before the first frame update
	private bool takeHiResShot = false;

	 public void TakeHiResShot() {
         takeHiResShot = true;
     }

	 public void SetScreenShot()
	 {
		_bgSet.SetActive(false);
		int tX = _camera.scaledPixelWidth;
		int tY = _camera.scaledPixelHeight;

		tempRT = new RenderTexture(tX, tY, 24, RenderTextureFormat.ARGB32)
		{
			antiAliasing = 4
		};
	
		_camera.targetTexture = tempRT;
	
		RenderTexture.active = tempRT;
		_camera.Render();

		Texture2D image = new Texture2D((int)_imageSize.x, (int)_imageSize.y, TextureFormat.ARGB32, false, true);
		
		float tXPos = tX*0.5f - image.width*0.5f;
		float tYPos = tY*0.5f - image.height*0.5f;

		image.ReadPixels(new Rect(tXPos, tYPos, image.width, image.height), 0, 0);
		image.Apply();
		
		RenderTexture.active = null;

		_textSaveList.Add(image);
		_bgSet.SetActive(true);

		
	 }

	 public void MakeScreenShotFile()
	 {
		ImageNumber++;
		int numX = ((int)_fullSize.x) / ((int)_imageSize.x);
		int numY = ((int)_fullSize.y) / ((int)_imageSize.y);
		int allSpriteNum = numX * numY;
		// Debug.Log(allSpriteNum);

		List<Texture2D> resultImages = new List<Texture2D>();
		resultImages.Add(new Texture2D((int)_fullSize.x, (int)_fullSize.y, TextureFormat.ARGB32, false, true));
		FillColorAlpha(resultImages[0]);

		int resultImageNum = 0;
		int rISave = allSpriteNum;

		int numXSave = numX;

		int tYNum = 1;
		int tXNum = -1;

		for(var i = 0 ; i < _textSaveList.Count; i++)
		{
			if(i == rISave)
			{
				tYNum = 1;
				resultImages.Add(new Texture2D((int)_fullSize.x, (int)_fullSize.y, TextureFormat.ARGB32, false, true));
				resultImageNum++;
				resultImages[resultImageNum].filterMode = FilterMode.Point;
				rISave += i;
				FillColorAlpha(resultImages[resultImageNum]);
			}

			Texture2D tTex = _textSaveList[i];
			tXNum++;

			for (int x = 0; x < tTex.width; x++)
			{
				for (int y = 0; y < tTex.height; y++)
				{
					Color bgColor = tTex.GetPixel(x, y);
					resultImages[resultImageNum].SetPixel(x + ((int)_imageSize.x) * tXNum, (((int)_fullSize.y) - ((int)_imageSize.y)*tYNum ) +  y , bgColor);
				}
			}

			if(i == numX-1) 
			{
				tYNum++;
				tXNum = -1;
				numX += numXSave;
			}

			
		}

		for(var i = 0 ; i < resultImages.Count;i++)
		{
			byte[] bytes = resultImages[i].EncodeToPNG();
			string tName ="";

			if(_imageName == "")
			{
				_imageName = _unitPrefab.name;
			}

			tName = _imageName;

			if(_separated)
			{
				tName = _imageName + "_" +ImageNumber+"_"+_sepaName;
			}
			else
			{
				tName = _imageName+"_Full";
			}

			string filename = string.Format("{0}/SPUM/ScreenShots/{2}_{1}.png", Application.dataPath,i,tName);
			Debug.Log(filename);
			System.IO.File.WriteAllBytes(filename, bytes);
		}

		takeHiResShot = false;
		_camera.targetTexture = null;
		_textSaveList.Clear();
		DestroyImmediate(tempRT);
	 }

	 public void PrintEndMessage()
	 {
		 _camera.targetTexture = null;
		 _textSaveList.Clear();
		 Debug.Log(string.Format("{0} Numbers Images Exported!!!", ImageNumber));
	 }

	 public static Texture2D FillColorAlpha(Texture2D tex2D, Color32? fillColor = null)
     {   
         if (fillColor ==null)
         {
             fillColor = Color.clear;
         }
         Color32[] fillPixels = new Color32[tex2D.width * tex2D.height];
         for (int i = 0; i < fillPixels.Length; i++)
         {
             fillPixels[i] = (Color32) fillColor;
         }
         tex2D.SetPixels32(fillPixels);
         return tex2D;
     }
}
