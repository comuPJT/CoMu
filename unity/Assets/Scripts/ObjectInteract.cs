using UnityEngine;
using System.Runtime.InteropServices;
using Photon.Pun;

public class ObjectInteract : MonoBehaviour
{
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

    private bool isTrigger = false; // 상호작용 범위 내에 있는지

    private Shader normal, outline;

    void Start()
    {
        // 테두리 굵기를 맞추기 위해 에셋에 따라 아웃라인 쉐이더를 다르게 설정
        outline = target.CompareTag("Record") ? Shader.Find("Sprites/Outline1") : Shader.Find("Sprites/Outline2");
        normal = Shader.Find("Sprites/Default");
    }

    void Update()
    {
        // 상호작용 가능 범위 내에서 X키를 누르면 기능 실행
        if (isTrigger && (Input.GetKeyDown(KeyCode.X) || Input.GetKeyUp(KeyCode.X)))
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
            SetUnityKeyboardInput("FALSE");
        }
    }

    private void OnTriggerEnter2D(Collider2D collision)
    {
        // 플레이어가 오브젝트와 가까워지면 테두리 표시하기
        if (collision.gameObject.GetPhotonView().IsMine)
        {
            target.GetComponent<SpriteRenderer>().material.shader = outline;

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
        if (collision.gameObject.GetPhotonView().IsMine)
        {
            target.GetComponent<SpriteRenderer>().material.shader = normal;

            // 상호작용 방법 안내 메시지 숨기기
            message.SetActive(false);

            isTrigger = false;
        }
    }

    private void SetUnityKeyboardInput(string str)
    {
        // 유니티 키보드 입력 활성화 또는 비활성화
        bool flag = str.Equals("TRUE");
        UnityEngine.WebGLInput.captureAllKeyboardInput = flag;
    }
}
