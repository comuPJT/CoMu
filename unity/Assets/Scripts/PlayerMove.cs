using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class PlayerMove : MonoBehaviour
{
    public float moveSpeed = 4.0f;
    Vector2 move = new Vector2();
    Rigidbody2D player;

    Animator animator;
    string animationState = "AnimationState";

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
}
