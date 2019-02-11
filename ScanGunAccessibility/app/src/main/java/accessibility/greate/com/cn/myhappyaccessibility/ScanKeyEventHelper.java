package accessibility.greate.com.cn.myhappyaccessibility;

import android.util.Log;
import android.view.KeyEvent;

/**
 * Created by li on 2018-06-12.
 */


public class ScanKeyEventHelper {
    public static final int MAX_KEYS_INTERVAL_DEFAULT = 20;
    private long currentTime = 0L;
    private boolean isKeySHIFT = false;
    private StringBuilder stringBuilder = new StringBuilder();
    private ScanGunCallBack callBack = null;
    private static int maxKeysInterval = 20;

    public static void setMaxKeysInterval(int interval) {
        maxKeysInterval = interval;
    }

    public ScanKeyEventHelper(ScanGunCallBack callBack) {
        this.callBack = callBack;
    }

    public boolean isMaybeScanning(int keyCode, KeyEvent event) {
        if (event.getFlags() != 8) {
            return false;
        } else {
            if (this.currentTime == 0L) {
                if (this.stringBuilder.length() > 0) {
                    this.stringBuilder = this.stringBuilder.delete(0, this.stringBuilder.length());
                }

                this.currentTime = System.currentTimeMillis();
            } else {
                if (System.currentTimeMillis() - this.currentTime > (long) maxKeysInterval && this.stringBuilder.length() > 0) {
                    this.stringBuilder = this.stringBuilder.delete(0, this.stringBuilder.length());
                }

                this.currentTime = System.currentTimeMillis();
            }

            checkLetterStatus(event);
            if (keyCode == 66) {
                 this.isKeySHIFT = false;
                this.currentTime = 0L;
                if (this.callBack != null) {
                    this.callBack.onScanFinish(this.stringBuilder.toString());
                }

                return true;
            } else {
                if (keyCode >= 7 && keyCode <= 16) {
                    this.handleTopNumKeys(keyCode);
                } else if (keyCode >= 29 && keyCode <= 54) {
                    this.checkShift((char) (keyCode + 68), (char) (keyCode + 36));
                } else {
                    if (keyCode < 144 || keyCode > 158) {
                        switch (keyCode) {
                            case 55:
                                this.checkShift(',', '<');
                                break;
                            case 56:
                                this.checkShift('.', '>');
                                break;
                            case 57:
                            case 58:
                            case 59:
                            case 60:
                            case 61:
                            case 63:
                            case 64:
                            case 65:
                            case 66:
                            case 67:
                            default:
                                return false;
                            case 62:
                                this.stringBuilder.append(' ');
                                break;
                            case 68:
                                this.checkShift('`', '~');
                                break;
                            case 69:
                                this.checkShift('-', '_');
                                break;
                            case 70:
                                this.checkShift('=', '+');
                                break;
                            case 71:
                                this.checkShift('[', '{');
                                break;
                            case 72:
                                this.checkShift(']', '}');
                                break;
                            case 73:
                                this.checkShift('\\', '|');
                                break;
                            case 74:
                                this.checkShift(';', ':');
                                break;
                            case 75:
                                this.checkShift('\'', '"');
                                break;
                            case 76:
                                this.checkShift('/', '?');
                        }

                        return true;
                    }

                    this.handleNumPadKeys(keyCode);
                }

                return true;
            }
        }
    }

    private void checkShift(char ascallNoShift, char ascallOnShift) {
        if (this.isKeySHIFT) {
            this.stringBuilder.append(ascallOnShift);
        } else {
            this.stringBuilder.append(ascallNoShift);
        }

    }
    private static final String TAG = "MyAccessibilityService";
    //检查shift键
    private void checkLetterStatus(KeyEvent event) {
        int keyCode = event.getKeyCode();

        if (keyCode == KeyEvent.KEYCODE_SHIFT_RIGHT || keyCode == KeyEvent.KEYCODE_SHIFT_LEFT) {
            if (event.getAction() == KeyEvent.ACTION_DOWN) {
                //按着shift键，表示大写
                //Log.e(TAG,"KeyEvent.ACTION_DOWN"+keyCode);
                this.isKeySHIFT = true;
            } else {
               // Log.e(TAG,"_____--KeyEvent.ACTION_DOWN"+keyCode);
                //松开shift键，表示小写
                this.isKeySHIFT = false;
            }
        }

    }

    private void handleNumPadKeys(int keyCode) {
        if (keyCode <= 153) {
            this.stringBuilder.append((char) (keyCode - 96));
        } else if (keyCode == 154) {
            this.stringBuilder.append('/');
        } else if (keyCode == 155) {
            this.stringBuilder.append('*');
        } else if (keyCode == 156) {
            this.stringBuilder.append('-');
        } else if (keyCode == 157) {
            this.stringBuilder.append('+');
        } else if (keyCode == 158) {
            this.stringBuilder.append('.');
        }

    }

    private void handleTopNumKeys(int keyCode) {
        if (keyCode >= 7 && keyCode <= 16) {
            switch (keyCode) {
                case 7:
                    this.checkShift('0', ')');
                    break;
                case 8:
                    this.checkShift('1', '!');
                    break;
                case 9:
                    this.checkShift('2', '@');
                    break;
                case 10:
                    this.checkShift('3', '#');
                    break;
                case 11:
                    this.checkShift('4', '$');
                    break;
                case 12:
                    this.checkShift('5', '%');
                    break;
                case 13:
                    this.checkShift('6', '^');
                    break;
                case 14:
                    this.checkShift('7', '&');
                    break;
                case 15:
                    this.checkShift('8', '*');
                    break;
                case 16:
                    this.checkShift('9', '(');
            }

        }
    }

    public interface ScanGunCallBack {
        void onScanFinish(String var1);
    }
}
