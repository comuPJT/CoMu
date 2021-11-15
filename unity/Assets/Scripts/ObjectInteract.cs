using UnityEngine;

public class ObjectInteract : MonoBehaviour
{
    public GameObject target;
    public GameObject message;

    private Shader normal, outline1, outline2;

    // Start is called before the first frame update
    void Start()
    {
        normal = Shader.Find("Sprites/Default");
        outline1 = Shader.Find("Sprites/Outline1");
        outline2 = Shader.Find("Sprites/Outline2");
    }

    // Update is called once per frame
    void Update()
    {

    }

    private void OnTriggerEnter2D(Collider2D collision)
    {
        // 플레이어가 오브젝트와 가까워지면 테두리 표시하기
        if (collision.gameObject.CompareTag("Player"))
        {
            if (target.CompareTag("Record"))
            {
                target.GetComponent<SpriteRenderer>().material.shader = outline1;
            }
            else
            {
                target.GetComponent<SpriteRenderer>().material.shader = outline2;
            }

            // 상호작용 방법 안내 메시지 표시하기
            message.SetActive(true);
            float tmp = gameObject.transform.position.y - target.GetComponent<RectTransform>().rect.height - 0.1f;
            message.transform.position = new Vector3(gameObject.transform.position.x, tmp, 0);
        }
    }

    private void OnTriggerExit2D(Collider2D collision)
    {
        // 플레이어가 오브젝트와 멀어지면 테두리 없애기
        if (collision.gameObject.CompareTag("Player"))
        {
            target.GetComponent<SpriteRenderer>().material.shader = normal;

            // 상호작용 방법 안내 메시지 숨기기
            message.SetActive(false);
        }
    }
}
