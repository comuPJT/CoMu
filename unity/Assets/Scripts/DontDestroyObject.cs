using UnityEngine;

public class DontDestroyObject : MonoBehaviour
{
    void Start()
    {
        var objs = FindObjectsOfType<DontDestroyObject>();
        if (objs.Length == 1)
        {
            DontDestroyOnLoad(gameObject);
        }
        else
        {
            Destroy(gameObject);
        }
    }

    void Update()
    {
        
    }
}
