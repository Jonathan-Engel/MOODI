package teammoodi.moodi;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;



public class LogoutConfirmationFragment extends DialogFragment {


    //
    //Public interface for classes to implement
    //
    public interface ILogoutListener {
        void Logout();
    }

    private ILogoutListener m_logoutListener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
       AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
       builder.setTitle("Log out?")
               .setMessage("Would you like to logout?")
               .setPositiveButton("Log out", new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialog, int which) {
                       //
                       //The code which invokes the callback
                       //
                       m_logoutListener.Logout();
                   }
               })
               .setCancelable(true)
               .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialog, int which) {
                       //
                       //Do nothing
                       //
                   }
               });

       return builder.create();
   }

   //
   //Setter for the callback
   //
   public void SetListener(ILogoutListener listener) {
        m_logoutListener = listener;
   }

}
