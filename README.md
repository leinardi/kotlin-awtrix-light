


```bash
sudo apt install vim git tig openjdk-17-jdk

sudo mkdir /opt/kal
sudo chown $USER:$USER /opt/kal/
scripts/fetch_release.py /opt/kal/

sudo cp systemd/kal.service /etc/systemd/system/
sudo systemctl enable kal.service
sudo systemctl start kal.service
sudo systemctl status kal.service

sudo cp systemd/turn_off_pi_led.service /etc/systemd/system/
sudo systemctl enable turn_off_pi_led.service
sudo systemctl start turn_off_pi_led.service
sudo systemctl status turn_off_pi_led.service
```
