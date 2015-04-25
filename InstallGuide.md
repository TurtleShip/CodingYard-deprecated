# Installation Guide

## Mac
### Frontend
1. Install nginx: ```brew install nginx```

2. Set up nginx
  1. Go to where nginx is installed : ```cd /usr/local/etc/nginx```

  2. Open nginx.conf.  At the very last line of nginx.conf, you will see ```include servers/*```. Move that line before ```}```, which should be a line or two above the ```include servers/*```

  3. Create ```codingyard.conf``` file under ```servers/``` directory, and copy and paste [this](frontend/nginx.conf).

3. Run nginx ```sudo nginx```

4. You can stop nginx by ```sudo nginx -s stop```


## Linux
Write me.
