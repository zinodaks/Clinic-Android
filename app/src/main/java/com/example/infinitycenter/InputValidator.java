package com.example.infinitycenter;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;

public class InputValidator {
    private boolean usernameError = false, ssnError = false, passwordError = false, fnameError = false, lnameError = false;

    public boolean getRegistrationErrors() {
        return usernameError && passwordError && ssnError && fnameError && lnameError;
    }

    private void validateUsername(final EditText username , final Button button) {
        username.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (username.getText().toString().trim().length() == 0) {
                    username.setError("Username required");
                    usernameError = false;
                } else if (username.getText().toString().trim().length() > 30) {
                    username.setError("Username too long");
                    usernameError = false;
                } else if (username.getText().toString().trim().matches("^[a-zA-Z0-9._-]")) {
                    username.setError("Only letters , numbers , dashes & underscores allowed");
                    usernameError = false;
                } else usernameError = true;
                activateButton(button);
            }
        });
    }

    private void validatePassword(final EditText password, final TextInputLayout lypassword , final EditText confirmPassword , final TextInputLayout lycomfirmpassword , final Button button) {
        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (password.getText().toString().trim().length() == 0 ) {
                    lypassword.setPasswordVisibilityToggleEnabled(false);
                    password.setError("Password required");
                    passwordError = false;
                } else if (!(password.getText().toString()).matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{8,}$")) {
                    password.setError("Must be at least 8 characters long containing at least one uppercase letter , lowercase letter and a digit.");
                    lypassword.setPasswordVisibilityToggleEnabled(false);
                    passwordError = false;
                } else {
                    lypassword.setPasswordVisibilityToggleEnabled(true);
                    passwordError = true;
                }
                activateButton(button);
            }
        });
        confirmPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if ((!password.getText().toString().trim().isEmpty()) && (!(confirmPassword.getText().toString().trim().equals(password.getText().toString().trim())))) {
                    confirmPassword.setError("Passwords do not match");
                    lycomfirmpassword.setPasswordVisibilityToggleEnabled(false);
                    passwordError = false;
                } else {
                    lycomfirmpassword.setPasswordVisibilityToggleEnabled(true);
                    passwordError = true;
                }
                activateButton(button);
            }
        });
    }

    private void validateSSN(final EditText ssn , final Button button) {

        ssn.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count == 1) {
                    if (start == 2 || start == 5) {
                        ssn.setText(ssn.getText() + "-");
                        ssn.setSelection(ssn.getText().length());
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (ssn.getText().toString().replaceAll("[\\s\\-()]", "").trim().length() < 9 || ssn.getText().toString().replaceAll("[\\s\\-()]", "").trim().length() > 9) {
                    ssn.setError("SSN should be 9 digits long");
                    ssnError = false;
                } else {
                    ssnError = true;
                }
                activateButton(button);
            }
        });
    }

    private void validateName(final EditText fname, final EditText lname , final Button button) {
        fname.addTextChangedListener(
                new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        if (fname.getText().toString().trim().length() == 0) {
                            fname.setError("First name required");
                            fnameError = false;
                        } else if (fname.getText().toString().trim().length() > 30) {
                            fname.setError("First name too long");
                            fnameError = false;
                        } else {
                            fnameError = true;
                        }
                    }
                });
        lname.addTextChangedListener(
                new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        if (lname.getText().toString().trim().length() == 0) {
                            lname.setError("Last name required");

                            lnameError = false;
                        } else if (lname.getText().toString().trim().length() > 30) {
                            lname.setError("Last name too long");
                            lnameError = false;
                        } else {
                            lnameError = true;
                        }
                        activateButton(button);
                    }
                });
    }

    public void RegistrationInputWatcher(final EditText username, final EditText fName,
                             final EditText lName, final EditText ssn, final EditText password, final TextInputLayout lypassword ,
                             final EditText confirmPassword  , final TextInputLayout lyconfirmpassword , final Button button) {
        validateUsername(username , button);
        validateName(fName, lName , button);
        validateSSN(ssn , button);
        validatePassword(password, lypassword , confirmPassword , lyconfirmpassword , button);
    }

    private void activateButton(Button button) {
        button.setEnabled(this.getRegistrationErrors());
    }

}
