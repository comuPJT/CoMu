using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.SceneManagement;

public class PlayerMove : MonoBehaviour
{
    public float moveSpeed = 5.0f;
    Vector2 move = new Vector2();
    Rigidbody2D player;

    Animator animator;
    string animationState = "AnimationState";

    public static Vector3 entryPosition = new Vector3();
    public static bool isExit = false;

    enum States
    {
        up = 1,
        down = 2,
        left = 3,
        right = 4
    }
    
    // Start is called before the first frame update
    void Start()
    {
        animator = GetComponent<Animator>();
        player = GetComponent<Rigidbody2D>();

        // 메인 씬으로 돌아가는 경우
        if (isExit)
        {
            // 캐릭터 위치를 입장했던 곳으로 설정
            player.transform.position = entryPosition;
            animator.SetInteger(animationState, (int)States.down);
        }
    }

    // Update is called once per frame
    void Update()
    {
        UpdateState();
    }

    private void FixedUpdate()
    {
        MoveCharacter();
    }

    private void MoveCharacter()
    {
        move.x = Input.GetAxisRaw("Horizontal");
        move.y = Input.GetAxisRaw("Vertical");

        // 일정 속력 유지를 위한 정규화
        move.Normalize();

        // 이동 속도에 맞춰서 이동
        player.velocity = move * moveSpeed;
    }

    private void UpdateState()
    {
        if (move.y > 0)
        {
            animator.SetInteger(animationState, (int)States.up);
        }
        else if (move.y < 0)
        {
            animator.SetInteger(animationState, (int)States.down);
        }
        else if (move.x > 0)
        {
            animator.SetInteger(animationState, (int)States.left);
        }
        else if (move.x < 0)
        {
            animator.SetInteger(animationState, (int)States.right);
        }
    }

    private void OnTriggerEnter2D(Collider2D collision)
    {
        if (collision.gameObject.CompareTag("Main"))
        {
            isExit = true;
            // 메인 씬으로 돌아오기
            SceneManager.LoadScene("Main", LoadSceneMode.Single);
        }
        else if (collision.gameObject.CompareTag("MyRoom")
            || collision.gameObject.CompareTag("Theme1")
            || collision.gameObject.CompareTag("Theme2")
            || collision.gameObject.CompareTag("Theme3")
            || collision.gameObject.CompareTag("Theme4")
            || collision.gameObject.CompareTag("Theme5"))
        {
            isExit = false;
            // 입장한 곳의 위치를 저장
            entryPosition.x = player.transform.position.x;
            entryPosition.y = player.transform.position.y - 1;
            // 입장한 문에 해당하는 씬으로 이동
            SceneManager.LoadScene(collision.gameObject.tag, LoadSceneMode.Single);
        }
    }
}
