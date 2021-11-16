using UnityEngine;
using System.Runtime.InteropServices;

public class ObjectInteract : MonoBehaviour
{
    // 키보드 입력 설정 확인
    [DllImport("__Internal")]
    private static extern bool GetInputActive();

    // 키보드 입력 설정 변경
    [DllImport("__Internal")]
    private static extern void SetInputInactive();

    // 플레이리스트 모달창 열기
    [DllImport("__Internal")]
    private static extern void OpenPlaylistModal();

    // 노래 신청 모달창 열기
    [DllImport("__Internal")]
    private static extern void OpenPlayListAddModal();

    // 오늘의 사연 모달창 열기
    [DllImport("__Internal")]
    private static extern void OpenTodayStoryModal();

    // 명예의 전당 모달창 열기
    [DllImport("__Internal")]
    private static extern void OpenBestStoryModal();

    // 캐릭터 선택 모달창 열기
    [DllImport("__Internal")]
    private static extern void OpenCharacterModal();

    public GameObject target;
    public GameObject message;

    private bool isTrigger; // 상호작용 범위 내에 있는지

    private Shader normal, outline1, outline2;

    void Start()
    {
        isTrigger = false;
        normal = Shader.Find("Sprites/Default");
        outline1 = Shader.Find("Sprites/Outline1");
        outline2 = Shader.Find("Sprites/Outline2");
    }

    void Update()
    {
        // 상호작용 가능 범위 내에서 X키를 누르면 기능 실행
        if (isTrigger && Input.GetKeyUp(KeyCode.X))
        {
            if (gameObject.CompareTag("PlayList"))
            {
                OpenPlaylistModal();
            }
            else if (gameObject.CompareTag("PlayListAdd"))
            {
                OpenPlayListAddModal();
            }
            else if (gameObject.CompareTag("TodayStory"))
            {
                OpenTodayStoryModal();
            }
            else if (gameObject.CompareTag("BestStory"))
            {
                OpenBestStoryModal();
            }
            else if (gameObject.CompareTag("Character"))
            {
                OpenCharacterModal();
            }

            // 모달창이 열리면 유니티에서의 키보드 입력 막기
            SetInputInactive();
            UnityEngine.WebGLInput.captureAllKeyboardInput = false;
        }

        // 유니티 키보드 입력 활성화 시키기
        if (GetInputActive())
        {
            UnityEngine.WebGLInput.captureAllKeyboardInput = true;
        }
    }

    private void OnTriggerEnter2D(Collider2D collision)
    {
        // 플레이어가 오브젝트와 가까워지면 테두리 표시하기
        if (collision.gameObject.CompareTag("Player"))
        {
            // 에셋에 따라 테두리 굵기 다르게 설정
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

            isTrigger = true;
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

            isTrigger = false;
        }
    }
}
