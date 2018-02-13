package teammoodi.moodi;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.view.View;
import android.content.Intent;

public class Dashboard extends AppCompatActivity {

    private Button SignUpButton;
    private Button LogInButton;
    private Button GotoRecord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        SignUpButton = findViewById(R.id.SignUpBotton);

        SignUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchSignUp();
            }
        });

        LogInButton = findViewById(R.id.LogInButton);
        LogInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchLogIn();
            }
        });

        GotoRecord = findViewById(R.id.gotorecordbutton);
        GotoRecord.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Record.class);
                startActivity(intent);
            }
        });
    }

    private void launchSignUp() {
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);
    }

    private void launchLogIn(){
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
}
